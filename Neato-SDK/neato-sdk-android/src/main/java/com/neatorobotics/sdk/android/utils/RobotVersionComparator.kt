package com.neatorobotics.sdk.android.utils

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
object RobotVersionComparator {
    /**
     * Compares two version strings.
     *
     * @param version1 a string of ordinal numbers separated by decimal points.
     * @param version2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     * The result is a positive integer if str1 is _numerically_ greater than str2.
     * The result is zero if the strings are _numerically_ equal.
     */
    @Throws(NumberFormatException::class)
    fun versionCompare(version1: String, version2: String): Int {
        //remove "-" first
        var version1_nodash = version1
        var version2_nodash = version2
        if (version1.contains("-")) {
            version1_nodash = version1.substring(0, version1.indexOf("-"))
        }
        if (version2.contains("-")) {
            version2_nodash = version2.substring(0, version2.indexOf("-"))
        }

        var parts1 = version1_nodash.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var parts2 = version2_nodash.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var size1 = parts1.size
        var size2 = parts2.size

        while (size1 != size2) {
            //update version adding .0
            if (size1 > size2) {
                version2_nodash += ".0"
            } else {
                version1_nodash += ".0"
            }

            parts1 = version1_nodash.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            parts2 = version2_nodash.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            size1 = parts1.size
            size2 = parts2.size
        }

        //same size go ahead
        for (i in 0 until size1) {
            if (Integer.parseInt(parts1[i]) > Integer.parseInt(parts2[i])) return 1
            if (Integer.parseInt(parts1[i]) < Integer.parseInt(parts2[i])) return -1
        }

        //if we are here the main version (x.y.z) are the same
        //so we have to compare the numbers after the dash if exist
        val dash1: Int
        val dash2: Int
        if (version1.contains("-")) {
            dash1 = Integer.parseInt(version1.substring(version1.indexOf("-") + 1, version1.length))
        } else {
            dash1 = Integer.MAX_VALUE
        }

        if (version2.contains("-")) {
            dash2 = Integer.parseInt(version2.substring(version2.indexOf("-") + 1, version2.length))
        } else {
            dash2 = Integer.MAX_VALUE
        }

        if (dash1 > dash2) return 1
        return if (dash1 < dash2)
            -1
        else
            0
    }
}

