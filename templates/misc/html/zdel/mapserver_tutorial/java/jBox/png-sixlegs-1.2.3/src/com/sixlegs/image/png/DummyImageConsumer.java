// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;

class DummyImageConsumer
implements ImageConsumer
{
    public void setDimensions(int width, int height)
    { }

    public void setProperties(Hashtable props)
    { }

    public void setHints(int hintflags)
    { }

    public void setPixels(int x, int y, int w, int h,
                          ColorModel model, byte pixels[], int off, int scansize)
    { }

    public void setPixels(int x, int y, int w, int h,
                          ColorModel model, int pixels[], int off, int scansize)
    { }

    public void imageComplete(int status)
    { }

    public void setColorModel(ColorModel model)
    { }
}
