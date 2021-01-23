package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import java.util.Random;

public class TargetedRandom extends Random {

    private int usageCount = 0;
    private int loopingLimit;
    private int targetCount;

    public TargetedRandom(long seed, int target, int current) {
        super(seed);
        this.loopingLimit = current;
        this.targetCount = target;
    }

    @Override
    protected int next(int bits) {
        ++this.usageCount;
        if (usageCount > loopingLimit) {
            while (usageCount++ <= targetCount) {
                super.next(bits);
            }
            usageCount = 1;
        }
        return super.next(bits);
    }

    public void skip(){
        next(1);
    }

    public void multiply() {
        usageCount = 0;
        loopingLimit *= 2;
        targetCount *= 2;
    }
}
