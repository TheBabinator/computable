package net.computable.computer;

import net.computable.ModMain;
import net.computable.block.util.EnergyHandler;

public class Computer {
    private final EnergyHandler energyHandler;
    public boolean indicator = false;
    public boolean halted = false;

    public Computer(EnergyHandler pEnergyHandler) {
        ModMain.LOGGER.debug("computer created!");
        energyHandler = pEnergyHandler;
    }

    public void tick() {
        if (Math.random() < 0.02) indicator = true;
        else if (Math.random() < 0.1) indicator = false;
        float drain = 0.2f;
        float consumed = energyHandler.consumeEnergy(drain, false);
        if (consumed != drain) {
            ModMain.LOGGER.debug("computer ran out of energy");
            halted = true;
        }
    }

    public void kill() {
        ModMain.LOGGER.debug("computer killed!");
    }
}
