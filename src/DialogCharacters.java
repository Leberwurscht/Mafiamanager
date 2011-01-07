/*
	Mafiamanager - a tool to support the referee of the parlor game "Mafia"
    Copyright (C) 2011  Thomas Högner

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DialogCharacters  extends JDialog implements ActionListener{

	private static final long serialVersionUID = -9021187337746013906L;
	
	// general
	private SortedMap<String, Player> playerlist;
	private int counterCharacters;
	private int counterPlayer;
	
	// gui
	private GridBagConstraints con;
	private KeyListener keylistener;
	
	private JLabel labelTxt;
	private JLabel labelCounter;
	private JPanel panelCharacters;
		private ArrayList<JPanel> panelx;
			private ArrayList<JLabel> labelx;
			private ArrayList<JTextField> fieldx;
	private JButton buttonAcc;
	
	public DialogCharacters(SortedMap<String, Player> _playerlist, JFrame _frame){
		super(_frame, true);
		
		// general
		playerlist = _playerlist;
		
		// gui
		setLayout(new GridBagLayout());
		con = new GridBagConstraints();
		con.gridx = 0;
		
		// text
		labelTxt = new JLabel(Messages.getString("gui.configureTxt"));
		con.gridy = 0;
		add(labelTxt, con);
		
		// counter
		counterPlayer = playerlist.size();
		
		labelCounter = new JLabel(Messages.getString("gui.leftPlayer")+" "+counterPlayer);
		con.gridy = 1;
		add(labelCounter, con);
		
		// labels and textfields for figures
		panelx = new ArrayList<JPanel>();
		labelx = new ArrayList<JLabel>();
		fieldx = new ArrayList<JTextField>();
		
		panelCharacters = new JPanel(new GridLayout(0,1));

		keylistener = new KeyListener() {
			public void keyPressed(KeyEvent event) {
			}
			public void keyReleased(KeyEvent event) {
				
				int sum = 0;
				boolean onlynumbers = true;
				
				for (JTextField curField : fieldx){
					String number = curField.getText();
					
					if (!number.equals("")){
						try {
							sum += Integer.parseInt(number);
						} catch (NumberFormatException e){
							System.err.println(Messages.getString("err.nonum"));
							onlynumbers = false;
							break;
						}
					}
				}
				
				counterPlayer = playerlist.size() - sum;
				
				if (onlynumbers && counterPlayer >= 0){
					labelCounter.setForeground(null);
					buttonAcc.setEnabled(true);
					labelCounter.setText(Messages.getString("gui.leftPlayer")+" "+counterPlayer);
				}
				else if (onlynumbers) {
					labelCounter.setForeground(Color.red);
					buttonAcc.setEnabled(false);
					labelCounter.setText(Messages.getString("gui.leftPlayer")+" "+counterPlayer);
				}
				else {
					labelCounter.setForeground(Color.red);
					buttonAcc.setEnabled(false);
					labelCounter.setText(Messages.getString("gui.leftPlayererr"));
				}

			}
			public void keyTyped(KeyEvent event) {
			}
		};
		
		counterCharacters = 0;
		addFigure(2);
		addFigure(3);
		addFigure(4);
		
		con.gridy = 2;
		add(panelCharacters, con);
		
		// button accept
		buttonAcc = new JButton(Messages.getString("gui.acc"));
		buttonAcc.addActionListener(this);
		con.gridy = 3;
		add(buttonAcc, con);
		
		pack();
		setLocationRelativeTo(_frame);
		setVisible(true);
		
	}
	
	// add figure
	public void addFigure(int _fig){
		
		panelx.add(new JPanel(new GridLayout(1,2)));
		labelx.add(new JLabel(Keys.IntToFigure(_fig)+":"));
		fieldx.add(new JTextField(5));
		fieldx.get(counterCharacters).addKeyListener(keylistener);
		
		panelx.get(counterCharacters).add(labelx.get(counterCharacters));
		panelx.get(counterCharacters).add(fieldx.get(counterCharacters));
		panelCharacters.add(panelx.get(counterCharacters));
		
		counterCharacters++;
	}

	public void actionPerformed(ActionEvent event) {
	
		if (event.getSource() == buttonAcc){
			Keys.villager = counterPlayer;
			
			Keys.mafia = StringToInt(fieldx.get(0).getText());
			Keys.detective = StringToInt(fieldx.get(1).getText());
			Keys.doctor = StringToInt(fieldx.get(2).getText());
			
			setVisible(false);
		}
		
	}
	
	public int StringToInt(String _s){
		int i = 0;
		
		if (!_s.equals("")){
			try {
				i = Integer.parseInt(_s);
			} catch (NumberFormatException e){
				System.err.println(Messages.getString("err.nonum"));
			}
		}
		
		return i;
	}
}
