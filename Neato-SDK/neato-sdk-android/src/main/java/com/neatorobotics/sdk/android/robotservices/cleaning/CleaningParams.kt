package com.neatorobotics.sdk.android.robotservices.cleaning

import com.neatorobotics.sdk.android.models.CleaningCategory
import com.neatorobotics.sdk.android.models.CleaningMode
import com.neatorobotics.sdk.android.models.CleaningModifier
import com.neatorobotics.sdk.android.models.NavigationMode

class CleaningParams(var category: CleaningCategory? = null,
                     var mode: CleaningMode? = null,
                     var modifier: CleaningModifier? = null,
                     var navigationMode: NavigationMode? = null,
                     var spotSize: Int? = null,
                     var zoneId: String? = null,
                     var zonesId: List<String>? = null
                     )