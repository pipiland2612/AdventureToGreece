package utils

import javax.sound.sampled.{AudioInputStream, AudioSystem, Clip, FloatControl}

class Sound:
  var clip: Clip = _
  var fc: FloatControl = _
  var volumeScale: Int = 3
  var volume: Float = _
  var soundUrls: Array[String] = Array(
    "/sounds/adventure.wav",
    "/sounds/boss_fight.wav"
  )

  def setFile(index: Int): Unit =
    val soundPath = soundUrls(index)
    val resourceStream = Option(getClass.getResourceAsStream(soundPath))

    resourceStream match
      case Some(stream) =>
        try
          val ais: AudioInputStream = AudioSystem.getAudioInputStream(stream)
          clip = AudioSystem.getClip()
          clip.open(ais)
          fc = clip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
          checkVolume()
        catch
          case e: Exception =>
            println(s"Error loading audio from $soundPath: ${e.getMessage}")
      case None =>
        println(s"Audio file not found at $soundPath")

  def play(): Unit =
    if (clip != null) then clip.start()

  def stop(): Unit =
    if (clip != null) then clip.stop()

  def loop(): Unit =
    if (clip != null) then clip.loop(Clip.LOOP_CONTINUOUSLY)

  def checkVolume(): Unit =
    volume = volumeScale match
      case 0 => -80f
      case 1 => -20f
      case 2 => -12f
      case 3 => -5f
      case 4 => 1f
      case 5 => 6f
    fc.setValue(volume)
end Sound
