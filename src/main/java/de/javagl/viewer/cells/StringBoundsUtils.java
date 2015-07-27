/*
 * www.javagl.de - Cells
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.viewer.cells;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;


// TODO This may be moved to the "Viewer" package, or maybe even to "Geom"

/**
 * Utility methods for computing string bounds
 */
class StringBoundsUtils
{
    /**
     * Computes an estimate of the bounds of the given string when it is 
     * rendered into the given graphics. <br>
     * <br>
     * When the size of the font of the graphics object is larger than a 
     * certain threshold (currently 5, but this may change), then the bounds 
     * of the string are estimated from the character widths and height of 
     * the FontMetrics of the graphics object. This may be significantly 
     * faster than computing the precise string bounds, as it is done
     * with {@link #computeStringBounds(String, Graphics2D)}<br>
     * <br>
     * If the given result rectangle is not <code>null</code>, it will 
     * be filled with the string bounds and returned. Otherwise, a new
     * rectangle will be created and returned. Note that depending on
     * the configuration (font size) of the graphics object, it may be
     * possible that a new rectangle has to be allocated anyhow, so 
     * passing in a non-<code>null</code> result can not guarantee that
     * no new allocations have to take place.
     *  
     * @param string The string
     * @param g The graphics
     * @param result The rectangle that will store the string bounds
     * @return The string bounds
     */
    static Rectangle2D computeStringBoundsEstimate(
        String string, Graphics2D g, Rectangle2D result)
    {
        Font font = g.getFont();
        if (font.getSize() > 5)
        {
            return computeStringBoundsFromFontMetrics(string, g, result);
        }
        Rectangle2D bounds = computeStringBounds(string, g);
        if (result != null)
        {
            result.setRect(bounds);
            return result;
        }
        return bounds;
    }
    
    /**
     * Computes an estimate of the bounds of the given string when it is 
     * rendered into the given graphics. <br>
     * <br>
     * The bounds that are returned are only good approximations when the
     * size of the font of the given graphics object is "large enough" 
     * so that the (integral) character widths that are returned by the
     * FontMetrics object are good approximations of the actual character
     * widths. <br>
     * <br>
     * If the given result rectangle is not <code>null</code>, then it
     * will be filled with the bounds and returned. Otherwise, a new
     * rectangle will be created and returned.
     * 
     * @param string The string
     * @param g The graphics
     * @param result The rectangle that will store the string bounds
     * @return The string bounds
     */
    private static Rectangle2D computeStringBoundsFromFontMetrics(
        String string, Graphics2D g, Rectangle2D result)
    {
        FontMetrics fontMetrics = g.getFontMetrics();
        double stringWidth = 0;
        for (int i=0; i<string.length(); i++)
        {
            stringWidth += fontMetrics.charWidth(string.charAt(i));
        }
        double stringHeight = fontMetrics.getHeight();
        if (result == null)
        {
            return new Rectangle2D.Double(
                0, -fontMetrics.getAscent(), stringWidth, stringHeight);
        }
        result.setRect(0, -fontMetrics.getAscent(), stringWidth, stringHeight);
        return result;
    }
    
    /**
     * Computes the bounds of the given string when it is rendered into the 
     * given graphics.<br> 
     * <br>
     * Note that depending on the configuration (font size) of the given 
     * graphics object, this may be much more expensive than
     * {@link #computeStringBoundsEstimate(String, Graphics2D, Rectangle2D)}.
     * 
     * @param string The string
     * @param g The graphics
     * @return The string bounds
     */
    static Rectangle2D computeStringBounds(
        String string, Graphics2D g)
    {
        Font font = g.getFont();
        if (font.getSize() > 0)
        {
            return computeStringBoundsDefault(string, g);
        }
        return computeStringBoundsFromNormalizedFont(string, g);
    }
    
    /**
     * Computes the bounds of the given string when it is rendered into the 
     * given graphics. <br>
     * <br>
     * This method only works properly for fonts whose integer size is larger
     * than 0 (i.e. whose float size is not smaller than 0.5f)
     * 
     * @param string The string
     * @param g The graphics
     * @return The string bounds
     */
    private static Rectangle2D computeStringBoundsDefault(
        String string, Graphics2D g)
    {
        FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle2D bounds = fontMetrics.getStringBounds(string, g);
        return bounds;
    }
    
    /**
     * Computes the bounds of the given string when it is rendered into the 
     * given graphics. <br>
     * <br>
     * This method is intended for cases where the font of the graphics 
     * object has a float size that is smaller than 0.5f
     * 
     * @param string The string
     * @param g The graphics
     * @return The string bounds
     */
    private static Rectangle2D computeStringBoundsFromNormalizedFont(
        String string, Graphics2D g)
    {
        Font font = g.getFont();
        double scaling = font.getSize2D();
        Font normalizedFont = font.deriveFont(1.0f);
        FontRenderContext fontRenderContext =
            new FontRenderContext(g.getTransform(), true, true);
        Rectangle2D normalizedBounds = 
            normalizedFont.getStringBounds(string, fontRenderContext);
        normalizedBounds.setRect(
            normalizedBounds.getX() * scaling,
            normalizedBounds.getY() * scaling,
            normalizedBounds.getWidth() * scaling,
            normalizedBounds.getHeight() * scaling);
        return normalizedBounds;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private StringBoundsUtils()
    {
        // Private constructor to prevent instantiation
    }
}
