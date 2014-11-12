/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sounddisplay;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Joseph
 */
public class SoundPanel extends JPanel{
    byte[] data = {0};
    int length = 1;
            
    public SoundPanel() {
        super();
        this.setSize(1000, 300);
    }
    
    public void updateDisplay(byte[] d, int l)
    {
        data = d;
        length = l;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < length; i++)
        {
            g.drawOval(i * 5, 150 + data[i] * 10, 3, 3);
        }
        
        g.drawLine(0, 150, 1000, 150);
    }
}
