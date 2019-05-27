/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.dsl

import org.junit.Assert
import org.junit.Test

class RobotDSLTest {

    @Test
    fun `test robot properties DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            linkedAt = "2014-01-02T12:00:02Z"
        }

        Assert.assertEquals("123", r.serial)
        Assert.assertEquals("mySecretKey", r.secret_key)
        Assert.assertEquals("2014-01-02T12:00:02Z", r.linkedAt)
    }

    @Test
    fun `test robot traits DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            traits {
                trait {
                    name = "maps"
                }
                trait {
                    name = "sampleTrait"
                }
            }
        }

        Assert.assertEquals(2, r.traits.size)
        Assert.assertTrue(r.traits.contains("maps"))
        Assert.assertTrue(r.traits.contains("sampleTrait"))
        Assert.assertFalse(r.traits.contains("xxx"))

    }

    @Test
    fun `test robot persistent maps DSL`() {

        val r = robot {
            serial = "123"
            secret_key = "mySecretKey"
            persistentMaps {
                persistentMap {
                    id = "1"
                    name = "My Home"
                }
                persistentMap {
                    id = "2"
                    name = "Mezzanine"
                }
            }
        }

        Assert.assertNotNull(r.persistentMapsIds)
        Assert.assertNotNull(r.persistentMapsNames)
        Assert.assertEquals(2, r.persistentMapsIds.size)
        Assert.assertEquals(2, r.persistentMapsNames.size)
        Assert.assertEquals("1", r.persistentMapsIds[0])
        Assert.assertEquals("2", r.persistentMapsIds[1])
        Assert.assertEquals("My Home", r.persistentMapsNames[0])
        Assert.assertEquals("Mezzanine", r.persistentMapsNames[1])
    }
}
