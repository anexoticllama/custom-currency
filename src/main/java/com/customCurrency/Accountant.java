package com.customCurrency;

import lombok.Getter;
import javax.inject.Inject;

import java.awt.image.BufferedImage;

import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.Counter;

public class Accountant extends Counter {

    @Getter
    private final String name;

    Accountant(Plugin p, int count, String pName, BufferedImage img){
        super(img, p, count);
        name = pName;
    }

    @Override
    public String getTooltip(){
        return name;
    }

}
