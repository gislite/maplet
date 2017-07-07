// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

/**
 * Interface to GIF Application Extension chunks (gIFx),
 * which constains application-specific information converted
 * from GIF89a files.
 * @see PngImage#getGifExtensions
 */
public interface GifExtension
{
    /** 
     * Returns the Application Identifier of this GifExtension,
     * which identifies the application that created the extension.
     */
    String getIdentifier();

    /** 
     * Returns a the Authentication Code of this GifExtension,
     * which may be used to further validate the extension.
     */
    byte[] getAuthenticationCode();

    /**
     * Returns the application-specific data of this GifExtension, which
     * is not defined by the GIF specification.
     */
    byte[] getData();
}
