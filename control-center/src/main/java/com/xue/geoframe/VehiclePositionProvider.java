package com.xue.geoframe;

import net.gcdc.geonetworking.Address;
import net.gcdc.geonetworking.LongPositionVector;
import net.gcdc.geonetworking.Position;
import net.gcdc.geonetworking.PositionProvider;
import org.threeten.bp.Instant;

/**
 * @author Xue
 * @create 2024-04-22 14:42
 */
public class VehiclePositionProvider implements PositionProvider {

    private final Address address;
    private Position position;
    private double speedMetersPerSecond;
    private double headingDegreesFromNorth;
    private Instant lastUpdateTimestamp;

    /**
     * VehiclePositionProvider constructor.
     *
     * @param address The vehicle address.
     */
    VehiclePositionProvider(Address address) {
        this.address = address;
        this.position = new Position(0, 0);
        this.speedMetersPerSecond = 0;
        this.headingDegreesFromNorth = 0;
        this.lastUpdateTimestamp = Instant.EPOCH;
    }

    /**
     * Update the stored vehicle position.
     *
     * @param latitude                The current latitude of the vehicle.
     * @param longitude               The current longitude of the vehicle.
     * @param speedMetersPerSecond    Vehicle speed in m/s.
     * @param headingDegreesFromNorth Heading in degrees from north.
     */
    public void update(
            double latitude,
            double longitude,
            double speedMetersPerSecond,
            double headingDegreesFromNorth) {
        this.position = new Position(latitude, longitude);
        this.speedMetersPerSecond = speedMetersPerSecond;
        this.headingDegreesFromNorth = headingDegreesFromNorth;
        this.lastUpdateTimestamp = Instant.now();
    }

    /**
     * Return the latest position of the vehicle.
     *
     * @return The latest position of the vehicle.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the latest position of the vehicle as a LongPositionVector.
     *
     * @return The latest position of the vehicle.
     */
    @Override
    public LongPositionVector getLatestPosition() {

        /* The position is considered confident for 200 ms after a
         * position update. */
        Instant timestamp = Instant.now();
        boolean isPositionConfident = timestamp.minusMillis(200).isBefore(lastUpdateTimestamp);

        return new LongPositionVector(
                address,
                timestamp,
                position,
                isPositionConfident,
                speedMetersPerSecond,
                headingDegreesFromNorth);
    }
}
