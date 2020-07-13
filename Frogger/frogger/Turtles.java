package frogger;
import jig.engine.util.Vector2D;

public class Turtles extends MovingEntity{
	
	private long underwaterTime   = 0;
	private long underwaterPeriod = 1200;
	
	protected boolean isUnderwater = false;
	
	// Animation variables
	private boolean isAnimating = false;
	private long localDeltaMs;
	private long startAnimatingMs;
	private long timerMs;
	private long animatingPeriod = 150; 
	private int aFrame = 0;
	private int max_aFrame = 2; // Animate only 2 frames
	
	/**
	 * Build a Turtle object that is floating by default
	 * 
	 * submerging still happens according to the period defined
	 * by the underwaterPeriod
	 * 
	 * @param pos
	 * @param v
	 */
	public Turtles (Vector2D pos, Vector2D v) {
		super(Main.SPRITE_SHEET + "#turtles");
		init(pos,v);
	}
	
	/**
	 * Build a submerged or floating object
	 * 
	 * @param pos
	 * @param v
	 * @param water - 0 submerged; 1 - floating
	 */
	public Turtles (Vector2D pos, Vector2D v, int water) {
		super(Main.SPRITE_SHEET + "#turtles");
		init(pos,v);
		
		
		// set submerged/floating state based on water variable
		if (water == 0) {
			isUnderwater = false;
		} else {
			isUnderwater = true;
			setFrame(getFrame()+2);
		}
		
	}
	
	/**
	 * Initializing the Turtles object
	 * 
	 * building collision spheres, etc
	 * 
	 * @param pos - position vector
	 * @param v   - velocity vector
	 */
	public void init(Vector2D pos, Vector2D v) {
		position = pos;
		Vector2D posSphere1 = position;
		Vector2D posSphere2 = new Vector2D(position.getX()+32, position.getY());
		Vector2D posSphere3 = new Vector2D(position.getX()+64, position.getY());
		collisionObjects.add(new CollisionObject("colSmall", posSphere1));
		collisionObjects.add(new CollisionObject("colSmall", posSphere2));
		collisionObjects.add(new CollisionObject("colSmall", posSphere3));
		velocity = v;
		
		// Turtles floating direction, left/right		
		if (v.getX() < 0)
			setFrame(0);
		else
			setFrame(3);		
	}

	
	/**
	 * Timer to go under water or float
	 */
	public void checkAirTime() {
		underwaterTime += localDeltaMs;  
		if (underwaterTime > underwaterPeriod) {
			underwaterTime = 0;
			startAnimation();
		}
	}
	
	
	/**
	 * Perform Animating sequence of submerging/floating
	 * based on abunch of local time variables at the top
	 */
	public void animate() {
		if (!isAnimating) 
			return;
		
		if (startAnimatingMs < timerMs) {
			startAnimatingMs = timerMs+animatingPeriod;
			
			if (isUnderwater)
				setFrame(getFrame()-1);
			else
				setFrame(getFrame()+1);

			aFrame++;
		}
		
		if (aFrame >= max_aFrame) {
			isAnimating = false;
			isUnderwater = !isUnderwater;
		}
	}
	
	/**
	 * Initiate the animation by reseting the animation variables
	 * 
	 * and flagging "isAnimationg"
	 */
	public void startAnimation() {
		isAnimating = true;
		startAnimatingMs = 0;
		timerMs = 0;
		aFrame = 0;
	}
	
	public void update(final long deltaMs) {
		super.update(deltaMs);
		localDeltaMs = deltaMs;
		timerMs += localDeltaMs;
	    checkAirTime();
	    animate();
	}
}