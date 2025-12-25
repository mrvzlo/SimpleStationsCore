package com.ave.simplestationscore.managers;

public class TemperatureManager {
    public static final int TotalCoolTime = 90;
    public static final float MaxTemp = 1800;
    public static final float MaxFeed = 1400;
    public static final int MinTemp = 1000;
    public static final int MinSegmentCoolTime = 3;
    public static final int SegmentCount = 9;
    private static final float SegmentSize = MaxTemp / SegmentCount;
    private static final float IncsTotalCount = SegmentCount * (SegmentCount - 1) / 2f;
    private static final float IncPerSegment = (TotalCoolTime - MinSegmentCoolTime * SegmentCount) / IncsTotalCount;

    public static float getCool(float current) {
        if (current < 20)
            return 0;
        return getSegmentCoolSpeed(getSegment(current));
    }

    private static float getSegmentCoolSpeed(int segment) {
        return SegmentSize / getSegmentCoolTime(segment) / 20f;
    }

    private static float getSegmentCoolTime(int segment) {
        return MinSegmentCoolTime + (SegmentCount - segment - 1) * IncPerSegment;
    }

    private static int getSegment(float current) {
        if (current == 0)
            return 0;
        return (int) ((current - 1) / SegmentSize);
    }
}
