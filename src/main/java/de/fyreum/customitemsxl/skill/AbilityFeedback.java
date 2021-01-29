package de.fyreum.customitemsxl.skill;

public enum AbilityFeedback {

    ACCESS_DENIED("access was denied."), // permission

    CANCELLED("the event got cancelled"), // the ItemAbilityCastEvent got cancelled

    EXCEPTION("casted ability threw an exception."), // exception during cast

    FAILED("failed to cast the ability."), // player did something wrong

    ON_COOLDOWN("couldn't cast because of an remaining cooldown."), // if an ability got an remaining cooldown on it

    SUCCESS("successfully casted the ability."), // successfully casted

    UNKNOWN("failed cast for an unknown reason."), // everything else

    ;

    private final String message;

    AbilityFeedback(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
