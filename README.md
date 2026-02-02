# FO Jump Meter (Android)

An Android mobile app that measures **vertical jump height** using phone sensors (accelerometer + gyroscope).  
It records each jump, shows detailed time-series charts, and estimates the mechanical work performed during take-off.


---

## Features

- **Jump height measurement**
  - maximum height `h_max` for each recorded jump
- **Charts**
  - vertical acceleration `a(t)`
  - vertical velocity `v(t)`
  - height over time `h(t)`
- **Work estimate**
  - `W = m · g · h_max` (based on user-provided mass)
- **Jump history**
  - save a jump under a chosen name and browse previous records

---

## How it works (high-level)

The core idea is to reconstruct the vertical motion of the jump from discrete sensor samples.

### 1) Coordinate system alignment (device → world vertical axis)

The accelerometer returns acceleration in the **device coordinate system** `(x, y, z)`, which changes orientation while the person moves.

To obtain vertical acceleration, the app projects the measured acceleration onto the **global vertical axis Z** using the rotation matrix `R` (from a sensor fusion / gyroscope-based rotation estimate):

```
a_vert = a_x · R[6] + a_y · R[7] + a_z · R[8]
```

Here, `R[6], R[7], R[8]` correspond to the components describing the world Z axis orientation in the device frame.

### 2) Numerical integration with Velocity Verlet

Sensor data arrives in discrete time steps `Δt`, so velocity and height must be computed numerically.

The app uses **Velocity Verlet** (more stable than Euler, reduces drift from cumulative integration error):

```
h_{n+1} = h_n + v_n Δt + 1/2 · a_n (Δt)^2
v_{n+1} = v_n + (a_n + a_{n+1})/2 · Δt
```

### 3) Motion phases & event detection

The algorithm operates as a small real-time state machine:

1. **Rest / Calibration**  
   The app estimates accelerometer offset (sensor bias) and subtracts it from subsequent samples.

2. **Take-off**  
   Recording starts when vertical acceleration exceeds an experimentally chosen threshold  
   (in our tests: **3.0 m/s²**).

3. **Flight**  
   Once airborne, the app assumes the only acceleration is gravity and ignores noisy sensor readings.  
   Integration uses `g ≈ 9.81 m/s²` until velocity reaches `v = 0` (apex).

4. **Landing**  
   Detected by a strong acceleration spike (impact) together with negative velocity.  
   After landing, the app finalizes the jump and resets integration state.

### 4) Work performed by the legs

Assuming kinetic energy is ~0 at the top of the jump, work is estimated from potential energy gain:

```
W = m · g · h_max
```

where `m` is the user’s mass (entered in the UI).

---

## Usage

### Measure a jump

1. Tap the **green start button**.
2. The app starts a **5-second countdown** so you can position the phone close to your body.
3. After a beep, the jump measurement begins.
4. The measurement stops automatically after landing.
5. You can name the jump to store it in your history.

Tips for more stable results:
- keep the **screen on** during measurement,
- after the beep, wait ~**1 second** before take-off (helps stabilize sensor sampling).

### Browse results

- Tap **My records** to see saved jumps.
- Open a jump to view charts (`h(t)`, `v(t)`, `a(t)`) and provide your mass to compute `W`.

---

## Known limitations (accuracy)

- Sensor sampling frequency on many phones can be **too low** for consistently accurate results.
- Results may be **overestimated**, especially for jumps with very sharp acceleration peaks (fast take-off).
- Double integration amplifies noise and drift; the app mitigates this using:
  - coordinate projection via rotation matrix,
  - Velocity Verlet,
  - a flight phase model using `g` instead of raw accelerometer values.

This is a measurement/estimation tool—not a calibrated lab instrument.

---

## Building & running

1. Open the project in **Android Studio**
2. Run on a physical Android device (sensor access required)
3. Grant sensor permissions if requested

---

## Demo
[Watch demo](./demo.mp4)





