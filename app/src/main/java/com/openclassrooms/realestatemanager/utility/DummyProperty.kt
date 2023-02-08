package com.openclassrooms.realestatemanager.utility

import com.openclassrooms.realestatemanager.models.RealEstate

class DummyProperty {
    companion object {

        fun getDummyProperty(): RealEstate {

            return RealEstate(
                    1, "Loft", 100, "Manhattan",
                    8, 4, Utils.description(),
                    "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                    0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                    "                                        \"Lafayette\\n\" +\n" +
                    "                                        \"NoHo\\n\" +\n" +
                    "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                    "23/08/2022", "", ""
            )

        }

        val samplePropertyList = mutableListOf(
                RealEstate(
                        2, "Penthouse", 250, "Queens",
                        8, 4, Utils.description(),
                        "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                        0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                        "                                        \"Lafayette\\n\" +\n" +
                        "                                        \"NoHo\\n\" +\n" +
                        "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                        "23/08/2022", "24/08/2022", "25/09/2022"
                ),

                RealEstate(
                        3, "House", 350, "Manhattan",
                        8, 4, Utils.description(),
                        "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                        0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                        "                                        \"Lafayette\\n\" +\n" +
                        "                                        \"NoHo\\n\" +\n" +
                        "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                        "23/08/2022", "26/09/2022", "27/10/2022"

                ),

                RealEstate(
                        4, "Flat", 450, "Manhattan",
                        10, 4, Utils.description(),
                        "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                        0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                        "                                        \"Lafayette\\n\" +\n" +
                        "                                        \"NoHo\\n\" +\n" +
                        "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                        "23/08/2022", "28/10/2022", "29/11/2022"

                ),

                RealEstate(
                        5, "Loft", 550, "Brooklyn",
                        11, 4, Utils.description(),
                        "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                        0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                        "                                        \"Lafayette\\n\" +\n" +
                        "                                        \"NoHo\\n\" +\n" +
                        "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                        "23/08/2022", "30/11/2022", "01/12/2022"

                ),

                RealEstate(
                        6, "Loft", 650, "Bronx",
                        12, 4, Utils.description(),
                        "https://www.notreloft.com/images/2016/10/loft-Manhattan-New-York-00500-800x533.jpg",
                        0, "", "", "41 Great Jones Street Penthouse\\n\" +\n" +
                        "                                        \"Lafayette\\n\" +\n" +
                        "                                        \"NoHo\\n\" +\n" +
                        "                                        \"New York", "", null,40.72896 ,-73.99279 ,
                        "23/08/2022", "02/12/2022", "01/01/2023"),


        )

    }

}