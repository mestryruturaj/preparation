package v0.creational.factory;

public class Main {
    public static void main(String[] args) {
        VehicleFactory vehicleFactory = new BikeFactory();
        Vehicle vehicle = vehicleFactory.createVehicle(27.87);
        vehicle.startEngine();
        vehicle.accelerate();
        vehicle.decelerate();
        System.out.println("Mileage is " + vehicle.getMileage());
        vehicle.stopEngine();
    }
}

interface Vehicle {
    public double getMileage();

    public void startEngine();

    public void stopEngine();

    public void accelerate();

    public void decelerate();
}

class Car implements Vehicle {
    private final double mileage;

    public Car(double mileage) {
        this.mileage = mileage;
    }

    @Override
    public double getMileage() {
        return this.mileage;
    }

    @Override
    public void startEngine() {
        System.out.println("Car engine started.");
    }

    @Override
    public void stopEngine() {
        System.out.println("Car engine stopped.");
    }

    @Override
    public void accelerate() {
        System.out.println("Car is accelerating.");
    }

    @Override
    public void decelerate() {
        System.out.println("Car is decelerating.");
    }
}

class Bike implements Vehicle {
    private final double mileage;

    public Bike(double mileage) {
        this.mileage = mileage;
    }

    @Override
    public double getMileage() {
        return this.mileage;
    }

    @Override
    public void startEngine() {
        System.out.println("Bike engine started.");
    }

    @Override
    public void stopEngine() {
        System.out.println("Bike engine stopped.");
    }

    @Override
    public void accelerate() {
        System.out.println("Bike is accelerating.");
    }

    @Override
    public void decelerate() {
        System.out.println("Bike is decelerating.");
    }
}

interface VehicleFactory {
    public Vehicle createVehicle(double mileage);
}

class CarFactory implements VehicleFactory {
    public Vehicle createVehicle(double mileage) {
        return new Car(mileage);
    }
}

class BikeFactory implements VehicleFactory {
    public Vehicle createVehicle(double mileage) {
        return new Bike(mileage);
    }
}


