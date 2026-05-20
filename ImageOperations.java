import java.awt.*;
import java.awt.image.BufferedImage;

class ImageOperations {

    /**
     * Removes the red channel from the image
     * @param img Image to process
     * @return New image with red channel zeroed out
     */
    static BufferedImage zeroRed(BufferedImage img) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = ColorOperations.getColorAtPos(img, j, i);
                newImg.setRGB(j, i, new Color(0, c.getGreen(), c.getBlue()).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Converts the image to grayscale
     * @param img Image to process
     * @return New grayscale image
     */
    static BufferedImage grayscale(BufferedImage img) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = ColorOperations.getColorAtPos(img, j, i);
                int lum = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                newImg.setRGB(j, i, new Color(lum, lum, lum).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Inverts the pixel data of the image
     * @param img Image to process
     * @return New inverted image
     */
    static BufferedImage invert(BufferedImage img) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = ColorOperations.getColorAtPos(img, j, i);
                int r = 255 - c.getRed();
                int g = 255 - c.getGreen();
                int b = 255 - c.getBlue();
                newImg.setRGB(j, i, new Color(r, g, b).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Mirrors the image either vertically or horizontally
     * @param img Image to process
     * @param dir Direction to mirror
     * @return New mirrored image
     */
    static BufferedImage mirror(BufferedImage img, MirrorMenuItem.MirrorDirection dir) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        if (dir == MirrorMenuItem.MirrorDirection.VERTICAL) {
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    if (j < img.getWidth() / 2) {
                        newImg.setRGB(j, i, img.getRGB(j, i));
                    } else {
                        newImg.setRGB(j, i, img.getRGB(img.getWidth() - 1 - j, i));
                    }
                }
            }
        } else {
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    if (i < img.getHeight() / 2) {
                        newImg.setRGB(j, i, img.getRGB(j, i));
                    } else {
                        newImg.setRGB(j, i, img.getRGB(j, img.getHeight() - 1 - i));
                    }
                }
            }
        }
        return newImg;
    }

    /**
     * Rotates the image 90 degrees clockwise or counterclockwise
     * @param img Image to process
     * @param dir Direction to rotate
     * @return New rotated image
     */
    static BufferedImage rotate(BufferedImage img, RotateMenuItem.RotateDirection dir) {
        BufferedImage newImg = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_RGB);
        if (dir == RotateMenuItem.RotateDirection.CLOCKWISE) {
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    newImg.setRGB(img.getHeight() - 1 - i, j, img.getRGB(j, i));
                }
            }
        } else {
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    newImg.setRGB(i, img.getWidth() - 1 - j, img.getRGB(j, i));
                }
            }
        }
        return newImg;
    }

    /**
     * Repeats the image n times horizontally or vertically
     * @param img Image to process
     * @param n Number of times to repeat
     * @param dir Direction to repeat
     * @return New repeated image
     */
    static BufferedImage repeat(BufferedImage img, int n, RepeatMenuItem.RepeatDirection dir) {
        if (dir == RepeatMenuItem.RepeatDirection.HORIZONTAL) {
            BufferedImage newImg = new BufferedImage(img.getWidth() * n, img.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    for (int z = 0; z < n; z++) {
                        newImg.setRGB(j + z * img.getWidth(), i, img.getRGB(j, i));
                    }
                }
            }
            return newImg;
        } else {
            BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight() * n, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    for (int z = 0; z < n; z++) {
                        newImg.setRGB(j, i + z * img.getHeight(), img.getRGB(j, i));                    }
                }

            }
            return newImg;
        }
    }

    /**
     * Zooms in on the image. The zoom factor increases in multiplicatives of 10% and
     * decreases in multiplicatives of 10%.
     *
     * @param img        the original image to zoom in on. The image cannot be already zoomed in
     *                   or out because then the image will be distorted.
     * @param zoomFactor The factor to zoom in by.
     * @return the zoomed in image.
     */
    static BufferedImage zoom(BufferedImage img, double zoomFactor) {
        int newImageWidth = (int) (img.getWidth() * zoomFactor);
        int newImageHeight = (int) (img.getHeight() * zoomFactor);
        BufferedImage newImg = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
        g2d.dispose();
        return newImg;
    }
}


