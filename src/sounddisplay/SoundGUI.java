/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sounddisplay;

import javax.swing.JFrame;

/**
 *
 * @author Joseph
 */
public class SoundGUI extends JFrame{
    private SoundPanel s = new SoundPanel();
    
    public SoundGUI() {
        super();
        
        this.setSize(1000, 300);        
        this.add(s);
        this.setVisible(true);
    } 
    
    public void updateDisplay(byte[] data, int length)
    {
        s.updateDisplay(data, length);
        s.repaint();
    }
}