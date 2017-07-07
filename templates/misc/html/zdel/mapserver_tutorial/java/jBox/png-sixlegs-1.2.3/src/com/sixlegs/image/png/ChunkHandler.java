// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

/**
 * A class implementing the <code>ChunkHandler</code> interface
 * can be registered using the <code>PngImage.registerChunk</code>
 * method. 
 * @see PngImage#registerChunk
 */
public interface ChunkHandler
{
    /**
     * Process chunk data.
     * This method is called upon encountering a chunk 
     * registered as being handled by the implementing class.
     * @see PngImage#registerChunk
     * @param type chunk type
     * @param data raw chunk data
     */
    void handleChunk(String type, byte[] data);
}
