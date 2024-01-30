/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascii;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Katze
 */
public class Convert
{

    private BufferedImage img;
    private final char[] asciiCode =
    {
        '!', '"', '#', '$', '%', '&', '(', ')', '*', '+', ' '
    };

    public void setImage(File file)
    {
        try
        {
            img = ImageIO.read(file);
        } catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    public boolean isImage()
    {
        try
        {
            img.getRGB(0, 0);
            return true;
        } catch (Exception ex)
        {
            return false;
        }
    }

    public int start()
    {
        img = resize();
        toGrey();
        return toAscii();
    }

    private BufferedImage resize()
    {
        int sizeX = 200;
        int sizeY = (100 * img.getHeight()) / img.getWidth();
        BufferedImage dimg = new BufferedImage(sizeX, sizeY, img.getType());
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(img, 0, 0, sizeX, sizeY, null);
        g2d.dispose();
        return dimg;
    }

    private void toGrey()
    {
        int mediaPixel, colorSRGB;
        Color colorAux;
        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++)
            {
                colorAux = new Color(img.getRGB(i, j));
                mediaPixel = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
                colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
                img.setRGB(i, j, colorSRGB);
            }
    }

    private int toAscii()
    {
        String asciitxt = "";
        Color color;
        FileWriter fw = null;
        File filecont;
        int cont = 0;
        while (true)
        {
            filecont = new File("imageASCII" + cont + ".txt");
            if (!filecont.isFile())
            {
                try
                {
                    filecont.createNewFile();
                } catch (IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            cont++;
        }
        try
        {
            fw = new FileWriter("imageASCII" + cont + ".txt");
        } catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        for (int i = 0; i < img.getHeight(); i++)
        {
            for (int j = 0; j < img.getWidth(); j++)
            {
                color = new Color(img.getRGB(j, i));
                int sum = 0;
                for (int k = 0; k <= 10; k++)
                {
                    if (color.getRed() >= sum && color.getRed() <= sum + 25.5)
                    {
                        asciitxt += asciiCode[k];
                        break;
                    }
                    sum += 25.5;
                }
            }
            asciitxt += "\n";
        }
        try
        {
            fw.write(asciitxt);
            fw.close();
        } catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        return cont;
    }
}
