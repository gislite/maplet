// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

abstract class Interlacer
{
    abstract int numPasses();
    abstract int getSpacingX(int pass);
    abstract int getSpacingY(int pass);
    abstract int getOffsetX(int pass);
    abstract int getOffsetY(int pass);

    private int maxSpacingX = 0;
    private int maxSpacingY = 0;
    private int widestPass = 0;

    protected int w, h;

    Interlacer(int w, int h)
    {
        this.w = w;
        this.h = h;
        int minSpacingX = w;
        int n = numPasses();
        for (int i = 0; i < n; i++) {
            int sp_x = getSpacingX(i);
            if (sp_x < minSpacingX) {
                minSpacingX = sp_x;
                widestPass = i;
            }
            maxSpacingX = Math.max(maxSpacingX, sp_x);
            maxSpacingY = Math.max(maxSpacingY, getSpacingY(i));
        }
    }

    final int getMaxSpacingX()
    {
        return maxSpacingX;
    }

    final int getMaxSpacingY()
    {
        return maxSpacingY;
    }

    final int getMaxPassWidth()
    {
        return getPassWidth(widestPass);
    }

    final int getPassWidth(int pass)
    {
        return ((w / maxSpacingX) * countPixelsX(pass, maxSpacingX) + 
                countPixelsX(pass, w % maxSpacingX));
    }

    final int getPassHeight(int pass)
    {
        return ((h / maxSpacingY) * countPixelsY(pass, maxSpacingY) + 
                countPixelsY(pass, h % maxSpacingY));
    }

    final int getBlockWidth(int pass)
    {
        return getSpacingX(pass) - getOffsetX(pass);
    }

    final int getBlockHeight(int pass)
    {
        return getSpacingY(pass) - getOffsetY(pass);
    }

    final int countPixelsX(int pass, int w)
    {
        int cur = 0;
        int next = getOffsetX(pass);
        int sp = getSpacingX(pass);
        for (int x = 0; x < w; x++) {
            if (x == next) {
                cur++;
                next = x + sp;
            }
        }
        return cur;
    }
  
    final int countPixelsY(int pass, int h)
    {
        int cur = 0;
        int next = getOffsetY(pass);
        int sp = getSpacingY(pass);
        for (int y = 0; y < h; y++) {
            if (y == next) {
                cur++;
                next = y + sp;
            }
        }
        return cur;
    }
}
