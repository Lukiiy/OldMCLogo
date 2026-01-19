package me.lukiiy.oldLogo;

import java.util.Random;

public class LogoEffectRandomizer {
    public double target;
    public double current;
    public double velocity;

    public LogoEffectRandomizer(Random random, int x, int y) {
        this.target = this.current = 10 + y + random.nextDouble() * 32 + x;
    }

    public void tick() {
        this.current = this.target;

        if (this.target > 0) velocity -= 0.6;
        this.target += velocity;
        velocity *= 0.9;

        if (this.target < 0) {
            this.target = 0;
            velocity = 0;
        }
    }
}
