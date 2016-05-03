package com.neatorobotics.sdk.android.constants;

/**
 * Created by Marco on 03/05/16.
 */
public class RobotStateConstants {

    public enum RobotState {
        IDLE,
        BUSY,
        PAUSED,
        ERROR,
        INVALID
    }

    public enum RobotAction {
        HOUSE_CLEANING,
        SPOT_CLEANING,
        MANUAL_CLEANING,
        DOCKING,
        MENU_ACTIVE,
        SUSPENDED_CLEANING,
        UPDATING,
        COPYING_LOGS,
        RECOVERY_LOCATION,
        INVALID
    }

    public enum RobotCleaningCategory {
        MANUAL(1),
        HOUSE(2),
        SPOT(3);
        private final int value;

        private RobotCleaningCategory(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningMode {
        ECO(1),
        TURBO(2);

        private final int value;

        private RobotCleaningMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningModifier {
        NORMAL(1),
        DOUBLE(2);

        private final int value;

        private RobotCleaningModifier(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum RobotCleaningSpotSize {
        NORMAL(200),
        DOUBLE(400);

        private final int value;

        private RobotCleaningSpotSize(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
