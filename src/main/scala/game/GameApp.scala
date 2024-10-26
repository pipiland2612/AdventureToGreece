package game

import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*

// RUN GAME HERE
object GameApp extends App :
  val frame = new JFrame("Adventure to Greece")
  val gamePanel = new GamePanel()

  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  frame.add(gamePanel, java.awt.BorderLayout.CENTER)
  frame.setSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight))
  setIcon()

  frame.addKeyListener(gamePanel.keyH)

  frame.pack()
  frame.setResizable(false)
  frame.setVisible(true)
  frame.setFocusable(true)
  frame.setLocationRelativeTo(null)

  gamePanel.config.loadConfig()
  gamePanel.setUpGame()
  gamePanel.startGameThread()

  def setIcon(): Unit =
    val icon: ImageIcon = new ImageIcon(ImageIO.read(new File("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Players/player_image.png")))
    frame.setIconImage(icon.getImage)