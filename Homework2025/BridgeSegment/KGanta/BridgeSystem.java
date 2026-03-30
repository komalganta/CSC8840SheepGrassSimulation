package Homework2025.BridgeSegment.KGanta;
import BridgeSegment.AbstractBridgeSystem;

//BridgeSystem:
//A system with three bridge segments connected in series

public class BridgeSystem extends AbstractBridgeSystem {

    public BridgeSystem(String name) {
        super(name);

        double crossingTime = 10.0; //crossing time

        //create bridge segments
        BridgeSegment bridge1 = new BridgeSegment(
                "BridgeSegment1",
                BridgeSystemSetting.Bridge1InitialState,
                BridgeSystemSetting.Bridge1TrafficLightDurationTime,
                crossingTime
        );

        BridgeSegment bridge2 = new BridgeSegment(
                "BridgeSegment2",
                BridgeSystemSetting.Bridge2InitialState,
                BridgeSystemSetting.Bridge2TrafficLightDurationTime,
                crossingTime
        );

        BridgeSegment bridge3 = new BridgeSegment(
                "BridgeSegment3",
                BridgeSystemSetting.Bridge3InitialState,
                BridgeSystemSetting.Bridge3TrafficLightDurationTime,
                crossingTime
        );

        //add all components
        add(westCarGenerator);
        add(eastCarGenerator);
        add(bridge1);
        add(bridge2);
        add(bridge3);
        add(transduser);

        //east cars flow: EastGen to bridge1 to bridge2 to bridge3
        addCoupling(eastCarGenerator, "out", bridge1, "car_in");
        addCoupling(bridge1, "car_out", bridge2, "car_in");
        addCoupling(bridge2, "car_out", bridge3, "car_in");

        //west cars flow: WestGen to bridge3 to bridge2 to bridge1
        addCoupling(westCarGenerator, "out", bridge3, "car_in");
        addCoupling(bridge3, "car_out2", bridge2, "car_in");
        addCoupling(bridge2, "car_out2", bridge1, "car_in");

        //transducer couplings for logging
        addCoupling(bridge1, "car_out", transduser, "Bridge1_EastOut");
        addCoupling(bridge1, "car_out2", transduser, "Bridge1_WestOut");

        addCoupling(bridge2, "car_out", transduser, "Bridge2_EastOut");
        addCoupling(bridge2, "car_out2", transduser, "Bridge2_WestOut");

        addCoupling(bridge3, "car_out", transduser, "Bridge3_EastOut");
        addCoupling(bridge3, "car_out2", transduser, "Bridge3_WestOut");
    }
}