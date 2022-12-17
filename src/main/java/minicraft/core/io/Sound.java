package minicraft.core.io;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tinylog.Logger;

// Creates sounds from their respective files
public class Sound { 

	// Player
	public static final Sound playerHurt 			= new Sound("/resources/sounds/mob/player/hurt.wav");
	public static final Sound playerDeath 			= new Sound("/resources/sounds/mob/player/death.wav");
	public static final Sound playerCraft 			= new Sound("/resources/sounds/mob/player/craft.wav");
	public static final Sound playerPickup			= new Sound("/resources/sounds/mob/player/pickup.wav");
	public static final Sound playerPlace 			= new Sound("/resources/sounds/mob/player/place.wav");
	public static final Sound playerChangeLevel 	= new Sound("/resources/sounds/mob/player/changelevel.wav");
	
	// Sheep
	public static final Sound sheepSay1 			= new Sound("/resources/sounds/mob/sheep/say1.wav");
	public static final Sound sheepSay2 			= new Sound("/resources/sounds/mob/sheep/say2.wav");
	public static final Sound sheepSay3 			= new Sound("/resources/sounds/mob/sheep/say3.wav");
	
	// Cow
	public static final Sound cowSay1 				= new Sound("/resources/sounds/mob/cow/say1.wav");
	public static final Sound cowSay2 				= new Sound("/resources/sounds/mob/cow/say2.wav");
	public static final Sound cowSay3 				= new Sound("/resources/sounds/mob/cow/say3.wav");
	
	// Pig
	public static final Sound pigSay1		 		= new Sound("/resources/sounds/mob/pig/say1.wav");
	public static final Sound pigSay2 				= new Sound("/resources/sounds/mob/pig/say2.wav");
	public static final Sound pigSay3 				= new Sound("/resources/sounds/mob/pig/say3.wav");
	
	// Chicken
	public static final Sound chickenSay1 			= new Sound("/resources/sounds/mob/chicken/say1.wav");
	public static final Sound chickenSay2 			= new Sound("/resources/sounds/mob/chicken/say2.wav");
	public static final Sound chickenSay3 			= new Sound("/resources/sounds/mob/chicken/say3.wav");
	
	// Air Wizard
	public static final Sound airWizardDeath 		= new Sound("/resources/sounds/mob/airwizard/bossdeath.wav");
	public static final Sound airWizardAttack 		= new Sound("/resources/sounds/mob/airwizard/wizardattack.wav");
	public static final Sound airWizardChangePhase 	= new Sound("/resources/sounds/mob/airwizard/changephase.wav");
	public static final Sound airWizardSpawnSpark 	= new Sound("/resources/sounds/mob/airwizard/spawnspark.wav");

	// Keeper
	public static final Sound Mob_keeper_death = new Sound("/resources/sounds/entities/Keeper/keeperdeath.wav");

	// Eye Queen
	public static final Sound Mob_eyeBoss_death = new Sound("/resources/sounds/entities/EyeQueen/eyedeath.wav");
	public static final Sound Mob_eyeBoss_changePhase = new Sound("/resources/sounds/entities/EyeQueen/changephase.wav");

	// Spawner
	public static final Sound Furniture_spawner_destroy = new Sound("/resources/sounds/entities/Spawner/destroy.wav");
	public static final Sound Furniture_spawner_destroy_2 = new Sound("/resources/sounds/entities/Spawner/destroy 2.wav");
	public static final Sound Furniture_spawner_destroy_3 = new Sound("/resources/sounds/entities/Spawner/destroy 3.wav");
	public static final Sound Furniture_spawner_spawn = new Sound("/resources/sounds/entities/Spawner/spawn.wav");
	public static final Sound Furniture_spawner_hurt = new Sound("/resources/sounds/entities/Spawner/hurt.wav");

	// Menu
	public static final Sound Menu_back = new Sound("/resources/sounds/gui/back.wav");
	public static final Sound Menu_select = new Sound("/resources/sounds/gui/select.wav");
	public static final Sound Menu_confirm = new Sound("/resources/sounds/gui/confirm.wav");
	public static final Sound Menu_loaded = new Sound("/resources/sounds/gui/loaded.wav");
	public static final Sound Menu_page_up = new Sound("/resources/sounds/gui/page up.wav");

	public static final Sound Intro = new Sound("/resources/sounds/music/title/Intro.wav");
	public static final Sound Intro2 = new Sound("/resources/sounds/music/title/Intro 2.wav");

	public static final Sound Amulet_locked = new Sound("/resources/sounds/music/amulet/Amulet locked.wav");
	public static final Sound Amulet_locked_2 = new Sound("/resources/sounds/music/amulet/Amulet locked 2.wav");
	public static final Sound Amulet_sucess = new Sound("/resources/sounds/music/amulet/Amulet sucess.wav");

	// Cave and ambience sounds
	public static final Sound Ambience1 = new Sound("/resources/sounds/ambient/Ambience1.wav");
	public static final Sound Ambience2 = new Sound("/resources/sounds/ambient/Ambience2.wav");
	public static final Sound Ambience3 = new Sound("/resources/sounds/ambient/Ambience3.wav");
	public static final Sound Ambience4 = new Sound("/resources/sounds/ambient/Ambience4.wav");
	public static final Sound Ambience5 = new Sound("/resources/sounds/ambient/Ambience5.wav");

	public static final Sound Sky_enviroment = new Sound("/resources/sounds/ambient/sky environment.wav");

	// Snow tile
	public static final Sound Tile_snow = new Sound("/resources/sounds/tiles/Snow/snow.wav");
	public static final Sound Tile_snow_2 = new Sound("/resources/sounds/tiles/Snow/snow 2.wav");
	public static final Sound Tile_snow_3 = new Sound("/resources/sounds/tiles/Snow/snow 3.wav");
	public static final Sound Tile_snow_4 = new Sound("/resources/sounds/tiles/Snow/snow 4.wav");

	// Farmland and Sky farmland tile
	public static final Sound Tile_farmland = new Sound("/resources/sounds/tiles/Farmland/farmland.wav");
	public static final Sound Tile_farmland_2 = new Sound("/resources/sounds/tiles/Farmland/farmland 2.wav");
	public static final Sound Tile_farmland_3 = new Sound("/resources/sounds/tiles/Farmland/farmland 3.wav");

	// Themes
	public static final Sound Theme_Fall = new Sound("/resources/sounds/music/fall.wav");
	public static final Sound Theme_Surface = new Sound("/resources/sounds/music/surface.wav");
	public static final Sound Theme_Peaceful = new Sound("/resources/sounds/music/peaceful.wav");
	public static final Sound Theme_Cave = new Sound("/resources/sounds/music/cave.wav");
	public static final Sound Theme_Cavern = new Sound("/resources/sounds/music/cavern.wav");
	public static final Sound Theme_Cavern_drip = new Sound("/resources/sounds/music/cavern drip.wav");
	
	// Generic hurt sound
	public static final Sound genericExplode 		= new Sound("/resources/sounds/genericExplode.wav");
	public static final Sound genericFuse 			= new Sound("/resources/sounds/genericFuse.wav");
	public static final Sound genericHurt 			= new Sound("/resources/sounds/genericHurt.wav");

    private Clip clip;
    private FloatControl volumeControl;
    private Thread fadeThread;

    public static void init() {
    	Logger.debug("Initializing sound engine ...");
    }
    
    private Sound(String name) {
    	Logger.debug("Loading sound clip: {}", name);
    	
        try {
            URL url = getClass().getResource(name);

            DataLine.Info info = new DataLine.Info(Clip.class, AudioSystem.getAudioFileFormat(url).getFormat());

            if (!AudioSystem.isLineSupported(info)) {
                Logger.error("ERROR: Audio format of file " + name + " is not supported: " + AudioSystem.getAudioFileFormat(url));

                System.out.println("Supported audio formats:");
                System.out.println("-source:");
                Line.Info[] sinfo = AudioSystem.getSourceLineInfo(info);
                Line.Info[] tinfo = AudioSystem.getTargetLineInfo(info);
                for (Line.Info value : sinfo) {
                    if (value instanceof DataLine.Info) {
                        DataLine.Info dataLineInfo = (DataLine.Info) value;
                        AudioFormat[] supportedFormats = dataLineInfo.getFormats();
                        for (AudioFormat af : supportedFormats) {
                            System.out.println(af);
                        }
                    }
                }
                System.out.println("-target:");
                for (int i = 0; i < tinfo.length; i++) {
                    if (tinfo[i] instanceof DataLine.Info) {
                        DataLine.Info dataLineInfo = (DataLine.Info) tinfo[i];
                        AudioFormat[] supportedFormats = dataLineInfo.getFormats();
                        for (AudioFormat af : supportedFormats) {
                            System.out.println(af);
                        }
                    }
                }

                return;
            }

            clip = (Clip) AudioSystem.getLine(info);
            clip.open(AudioSystem.getAudioInputStream(url));
            
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(AudioSystem.getAudioInputStream(url));
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    clip.flush();
                    clip.setFramePosition(0);
                }
            });

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            Logger.error("Could not load sound file " + name);
            e.printStackTrace();
        }
    }
    
    // NOTE, this is a headcache, try not play lot sounds with this at same time ._.
    public void playOnWorld(int x, int y, int playerX, int playerY) {
        if (!(boolean) Settings.get("sound")) {
            return;
        }

        // Calculate the distance between the sound and the player
        double distance = Math.sqrt(Math.pow(x - playerX, 2) + Math.pow(y - playerY, 2));

        // Set the volume based on the distance from the player
        float minVolume = volumeControl.getMinimum();
        float volume = 1.0f - (float) distance / 21.0f; // Start to fade after of 28 (distance)
        if (volume < minVolume) { // CHECK
            volume = minVolume;
        }
        volumeControl.setValue(volume);

        float fadeRate = 0.1f; // Adjust this value to control the rate at which the sound fades

        clip.start();

        // Start a separate thread to gradually fade the sound as the player moves away
        fadeThread = new Thread(() -> {
            while (volumeControl.getValue() > volumeControl.getMinimum() && clip.isRunning()) {
                // Calculate the new volume based on the current volume and the fade rate
                float newVolume = volumeControl.getValue() - (0.1f * fadeRate);
                // Make sure the new volume is above the minimum allowable value
                if (newVolume > volumeControl.getMinimum()) {
                    volumeControl.setValue(newVolume);
                }
                try {
                    Thread.sleep(2); // Adjust this value to control how often the volume is decreased
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            clip.stop();
        });
        fadeThread.start();
    }



    public void playOnGui() {
        if (!(boolean) Settings.get("sound") || clip == null) {
            return;
        }

        if (clip.isRunning() || clip.isActive()) {
            clip.stop();
        }

        clip.start();
    }


    public void loop(boolean start) {
        if (start) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.stop();
        }
    }

    public void stop() {
        clip.stop();
    }
}