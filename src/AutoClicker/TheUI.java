package AutoClicker;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.logging.LogManager;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;



public class TheUI implements NativeMouseListener, NativeKeyListener {
	public static void main(String[] args) throws NativeHookException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, AWTException {
		TheUI GUI = new TheUI();
		//KillSwitch ks = new KillSwitch();
		//ks.run();
		LogManager.getLogManager().reset();
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeMouseListener(GUI);
		GlobalScreen.addNativeKeyListener(GUI);

		System.out.println(GlobalScreen.isNativeHookRegistered());
	}
	private final ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("Resources/icon.png"));
	private Robot rb;
	private int x = 0, y = 0;
	private JFrame frame;
	private JPanel panel;
	private JPanel dragbar;
	private JLabel title;
	private JLabel exit;
	static JToggleButton toggleButton;
	private JSlider slider;

	private boolean insaneMode = false;
	private boolean isEnabled = false;
	private int aimCPS = 20;
	/**
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws AWTException 
	 * @wbp.parser.entryPoint
	 */
	public TheUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, AWTException {
		rb = new Robot();

		LookAndFeel lf = UIManager.getLookAndFeel();
		frame = new JFrame("AutoClicker");
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(72, 140, 236));
		frame.getContentPane().add(panel);
		frame.setSize(120, 145);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setLocation(100, 100);
		frame.setIconImage(icon.getImage());
		frame.setVisible(true);

		dragbar = new JPanel();
		panel.add(dragbar);
		dragbar.setLayout(null);
		dragbar.setSize(panel.getWidth(), 15);
		dragbar.setBackground(new Color(0, 119, 182));
		dragbar.addMouseListener(new DragBarMouseListener());
		dragbar.addMouseMotionListener(new DragBarMouseMotionListener());

		Font f = new Font("Consola", Font.ITALIC, 8);
		title = new JLabel("  AutoClicker \"Premium\" (20)");
		dragbar.add(title);
		title.setFont(f);
		title.setForeground(new Color(255, 255, 255));
		title.setSize(dragbar.getWidth(), dragbar.getHeight());

		Font f2 = new Font("Consola", Font.PLAIN, 15);
		exit = new JLabel("×", SwingConstants.CENTER);
		dragbar.add(exit);
		exit.setFont(f2);
		exit.setOpaque(true);
		exit.setBackground(null);
		exit.setSize(dragbar.getHeight(), dragbar.getHeight());
		exit.setLocation(dragbar.getWidth()-exit.getWidth(), 0);
		exit.addMouseListener(new ExitMouseListener());

		toggleButton = new JToggleButton();
		panel.add(toggleButton);
		toggleButton.setText("OFF");
		toggleButton.setSize(100, 100);
		toggleButton.setSelected(true);
		toggleButton.setLocation(10, 20);
		toggleButton.setFocusable(false);
		toggleButton.addActionListener(new ToggleButtonActionListener());
		toggleButton.setBackground(new Color(184, 227, 255));
		toggleButton.setBorder(null);

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		slider = new JSlider(5, 100);
		UIManager.setLookAndFeel(lf);
		panel.add(slider);
		slider.setValue(20);
		slider.setFocusable(false);
		slider.setBackground(null);
		slider.setLocation(10, 125);
		slider.setForeground(Color.GREEN);
		slider.setSize(toggleButton.getWidth(), 15);
		slider.addChangeListener(new SliderChangeListener());

	}
	private class DragBarMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			frame.setLocation(e.getXOnScreen()-x, e.getYOnScreen()-y);
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
	}

	private class DragBarMouseListener implements MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	private class ExitMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.exit(0);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			exit.setBackground(new Color(0, 69, 132));
			exit.setForeground(new Color(100, 100, 100));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			exit.setBackground(null);
			exit.setForeground(new Color(0, 0, 0));
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	private class ToggleButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(toggleButton.isSelected()) {
				toggleButton.setText("OFF");
			}else {
				toggleButton.setText("ON");
			}
			isEnabled = !isEnabled;
			System.out.println("Activated: " + isEnabled);
		}
	}

	private class SliderChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			aimCPS = slider.getValue();
			if(insaneMode) {
				title.setText("  AutoClicker \"INSANE\" (" + String.valueOf(slider.getValue()) + ")");
			}else {

				title.setText("  AutoClicker \"Premium\" (" + String.valueOf(slider.getValue()) + ")");
			}
			System.out.println(slider.getValue() + " CPS");
		}
	}

	private void autoClick(int aimCPS, int range) throws InterruptedException, NativeHookException {
		int min = aimCPS - range;
		int max = aimCPS + range;
		Random rnd = new Random();
		int numberOfClicks = rnd.nextInt(max-min)+min;
		int wait = 1000/max;
		GlobalScreen.unregisterNativeHook();
		for(int i = 0; i < numberOfClicks; i++) {
			if(isEnabled) {
				rb.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(wait);
				rb.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
				System.out.println("Click #" + i);
			}else {
				break;
			}
		}
		GlobalScreen.registerNativeHook();
		dipShit();
	}

	private void dipShit() throws NativeHookException {
		while(!GlobalScreen.isNativeHookRegistered()) {
			GlobalScreen.registerNativeHook();
			System.out.println("Fuck you");
		}
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
		if(arg0.getButton() != MouseEvent.BUTTON1 && arg0.getButton() != MouseEvent.BUTTON2) {
			System.out.println("Other Click Detected");
			toggleButton.doClick();
		}else if(arg0.getButton() == MouseEvent.BUTTON1) {
			System.out.println("Left Click Detected");
			if(isEnabled) {
				try {
					autoClick(aimCPS, 3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {}
	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {}
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		if(arg0.getKeyCode() == 11) {
			if(insaneMode) {
				slider.setMaximum(100);
			}else {
				slider.setMaximum(1000);
				title.setText("  AutoClicker \"INSANE\" (" + String.valueOf(slider.getValue()+5) + ")");
			}
			insaneMode = !insaneMode;
			System.out.println("Insane Mode: " + insaneMode);
		}
	}
	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {}
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}
}
