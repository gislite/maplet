// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

final class Adam7Interlacer
extends Interlacer
{
    static final int[] init = { 0, 0, 4, 0, 2, 0, 1, 0 };
    static final int[] sp = { 8, 8, 8, 4, 4, 2, 2, 1 };

    Adam7Interlacer(int w, int h)
    {
        super(w, h);
    }

    final int numPasses ()
    {
        return 7;
    }

    final int getSpacingX(int pass)
    {
        return sp[pass+1];
    }

    final int getSpacingY(int pass)
    {
        return sp[pass];
    }

    final int getOffsetX(int pass)
    {
        return init[pass+1];
    }

    final int getOffsetY(int pass)
    {
        return init[pass];
    }
}
