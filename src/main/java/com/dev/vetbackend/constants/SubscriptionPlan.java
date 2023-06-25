package com.dev.vetbackend.constants;

public enum SubscriptionPlan {
    NO_PLAN("no_plan", 10),
    BASIC("basic", 1000),
    PLUS("plus", Integer.MAX_VALUE),
    PREMIUM("premium", Integer.MAX_VALUE);

    private final String planId;
    private final int maxPetsAllowed;

    SubscriptionPlan(String planId, int maxPetsAllowed) {
        this.planId = planId;
        this.maxPetsAllowed = maxPetsAllowed;
    }

    public String getPlanId() {
        return planId;
    }

    public int getMaxPetsAllowed() {
        return maxPetsAllowed;
    }

    public static SubscriptionPlan fromPlanId(String planId) {
        for (SubscriptionPlan plan : SubscriptionPlan.values()) {
            if (plan.getPlanId().equalsIgnoreCase(planId)) {
                return plan;
            }
        }
        throw new IllegalArgumentException("Invalid plan ID: " + planId);
    }

}
