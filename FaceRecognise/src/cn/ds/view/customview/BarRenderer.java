/**
 * Project             :FaceRecognise project
 * Comments            :自定义Jtable列为JProgressBar
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.view.customview;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BarRenderer extends JProgressBar implements TableCellRenderer {

	public BarRenderer()  {
	         super(0,100);     
	         this.setForeground(new Color(45,147,192));
	         this.setStringPainted(true);
	         this.setBorderPainted(false);
	    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	setStringPainted(true);
        setValue(((Integer) value).intValue());
        return this;
    }
}
