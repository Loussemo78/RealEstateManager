import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import java.io.*


class FileUtils(context: Context) {
     val context: Context

    init {
        this.context = context
    }

    @SuppressLint("NewApi")
    fun getPath(uri: Uri): String? {
        // check here to KITKAT or new version
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // DocumentProvider
        if (isKitKat) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                val fullPath = getPathFromExtSD(split)
                return if (fullPath !== "") fullPath else null
            }


            // DownloadsProvider
            if (isDownloadsDocument(uri)) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val id: String
                var cursor: Cursor? = null
                try {
                    cursor = context.getContentResolver()
                        .query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val fileName: String = cursor.getString(0)
                        val path: String = Environment.getExternalStorageDirectory()
                            .toString() + "/Download/" + fileName
                        if (!TextUtils.isEmpty(path)) return path
                    }
                } finally {
                    if (cursor != null) cursor.close()
                }
                id = DocumentsContract.getDocumentId(uri)
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) return id.replaceFirst("raw:".toRegex(), "")
                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                    )
                    for (contentUriPrefix in contentUriPrefixesToTry) return try {
                        val contentUri: Uri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix),
                            java.lang.Long.valueOf(id)
                        )
                        getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        //In Android 8 and Android P the id is not a number
                        uri.getPath()?.replaceFirst("^/document/raw:", "")?.replaceFirst("^raw:", "")
                    }
                }
            } else {
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) return id.replaceFirst("raw:".toRegex(), "")
                try {
                    contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                if (contentUri != null) return getDataColumn(context, contentUri, null, null)
            }


            // MediaProvider
            if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) contentUri =
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI else if ("video" == type) contentUri =
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI else if ("audio" == type) contentUri =
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
            if (isGoogleDriveUri(uri)) return getDriveFilePath(uri)
            if (isWhatsAppFile(uri)) return getFilePathForWhatsApp(uri)
            if ("content".equals(uri.getScheme(), ignoreCase = true)) {
                if (isGooglePhotosUri(uri)) return uri.getLastPathSegment()
                if (isGoogleDriveUri(uri)) return getDriveFilePath(uri)
                // return getFilePathFromURI(context,uri);
                // return getRealPathFromURI(context,uri);
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) copyFileToInternalStorage(
                    uri,
                    "userfiles"
                ) else getDataColumn(context, uri, null, null)
            }
            if ("file".equals(uri.getScheme(), ignoreCase = true)) return uri.getPath()
        } else {
            if (isWhatsAppFile(uri)) return getFilePathForWhatsApp(uri)
            if ("content".equals(uri.getScheme(), ignoreCase = true)) {
                val projection = arrayOf(
                    MediaStore.Images.Media.DATA
                )
                var cursor: Cursor? = null
                try {
                    cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
                    val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (cursor != null) {
                        if (cursor.moveToFirst()) if (cursor != null) {
                            return column_index?.let { cursor.getString(it) }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    private fun fileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private fun getPathFromExtSD(pathData: Array<String>): String {
        val type = pathData[0]
        val relativePath = "/" + pathData[1]
        var fullPath = ""

        // on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
        // something like "71F8-2C0A", some kind of unique id per storage
        // don't know any API that can get the root path of that storage based on its id.
        //
        // so no "primary" type, but let the check here for other devices
        if ("primary".equals(type, ignoreCase = true)) {
            fullPath = Environment.getExternalStorageDirectory().toString() + relativePath
            if (fileExists(fullPath)) return fullPath
        }

        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
        if (fileExists(fullPath)) return fullPath
        fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
        return if (fileExists(fullPath)) fullPath else fullPath
    }

    private fun getDriveFilePath(uri: Uri): String {
        val returnUri: Uri = uri
        val returnCursor: Cursor? =
            context.contentResolver.query(returnUri, null, null, null, null)
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        val nameIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
        returnCursor?.moveToFirst()
        val name: String? = nameIndex?.let { returnCursor?.getString(it) }
        val size = returnCursor?.let {
            if (sizeIndex != null) {
                it.getLong(sizeIndex).toString()
            }
        }
        val file = File(context.cacheDir, name)
        try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)!!
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) outputStream.write(
                buffers,
                0,
                read
            )
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: Exception) {
            e.message?.let { Log.e("Exception", it) }
        }
        return file.path
    }

    /***
     * Used for Android Q+
     * @param uri
     * @param newDirName if you want to create a directory, you can set this variable
     * @return
     */
    private fun copyFileToInternalStorage(uri: Uri, newDirName: String): String {
        val returnUri: Uri = uri
        val returnCursor: Cursor? = context.contentResolver.query(
            returnUri, arrayOf(
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
            ), null, null, null
        )


        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        val nameIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
        returnCursor?.moveToFirst()
        val name: String? = nameIndex?.let { returnCursor.getString(it) }
        val size = returnCursor?.let {
            if (sizeIndex != null) {
                it.getLong(sizeIndex).toString()
            }
        }
        val output: File
        if (newDirName != "") {
            val dir = File(context.filesDir , newDirName)
            if (!dir.exists()) dir.mkdir()
            output = File(context.filesDir , "/$newDirName/$name")
        } else output = File(context.filesDir , "/$name")
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(output)
            var read = 0
            val bufferSize = 1024
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) outputStream.write(
                buffers,
                0,
                read
            )
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            e.message?.let { Log.e("Exception", it) }
        }
        return output.path
    }

    private fun getFilePathForWhatsApp(uri: Uri): String {
        return copyFileToInternalStorage(uri, "whatsapp")
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = uri?.let {
                context.contentResolver.query(
                    it, projection,
                    selection, selectionArgs, null
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return if (uri.authority != null) "com.android.externalstorage.documents" == uri.authority else false
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return if (uri.authority != null) "com.android.providers.downloads.documents" == uri.authority else false
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return if (uri.authority != null) "com.android.providers.media.documents" == uri.authority else false
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return if (uri.authority != null) "com.google.android.apps.photos.content" == uri.authority else false
    }

    private fun isWhatsAppFile(uri: Uri): Boolean {
        return if (uri.authority != null) "com.whatsapp.provider.media" == uri.authority else false
    }

    private fun isGoogleDriveUri(uri: Uri): Boolean {
        return if (uri.authority != null) "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority else false
    }

    companion object {
        private var contentUri: Uri? = null
        @Throws(IOException::class)
        fun copy(src: File?, dst: File?) {
            FileInputStream(src).use { `in` ->
                FileOutputStream(dst).use { out ->
                    // Transfer bytes from in to out
                    val buf = ByteArray(1024)
                    var len: Int
                    while (`in`.read(buf).also { len = it } > 0) out.write(buf, 0, len)
                }
            }
        }
    }
}