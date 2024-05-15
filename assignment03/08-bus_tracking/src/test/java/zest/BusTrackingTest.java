package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.*;
import static org.mockito.Mockito.*;

import java.util.List;

class BusTrackingTest {

    @Mock
    private GPSDeviceService gpsService;
    @Mock
    private MapService mapService;
    @Mock
    private NotificationService notificationService;
    private BusTracker busTracker;
    @Captor
    private ArgumentCaptor<String> busIdCaptor;
    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        busTracker = new BusTracker(gpsService, mapService, notificationService);
    }

    @Test
    void test1updateLocation(){
        String busId = "bus1";
        Location locationStub = new Location(40.404040, 50.505050, false, "");
        when(gpsService.getCurrentLocation(busId)).thenReturn(locationStub);

        busTracker.updateBusLocation(busId);
        verify(mapService).updateMap(busId, locationStub);
    }

    @Test
    void test2nullLocation(){
        String busId = "bus2";
        when(gpsService.getCurrentLocation(busId)).thenReturn(null);

        busTracker.updateBusLocation(busId);

        String expected = "GPS signal lost. Please check back later.";
        verify(notificationService).notifyPassengers(busId, expected);
    }

    @Test
    void test3marginalLocationUpdate(){
        String busId = "bus3";
        Location initialLocation = new Location(40.404040, 50.505050, false, "");
        Location slightlyDifferentLocation = new Location(40.404041, -50.505050, false, "");

        when(gpsService.getCurrentLocation(busId)).thenReturn(initialLocation).thenReturn(slightlyDifferentLocation);

        busTracker.updateBusLocation(busId);
        busTracker.updateBusLocation(busId);

        verify(mapService).updateMap(busId, initialLocation);
        verify(mapService).updateMap(busId, slightlyDifferentLocation);
    }

    @Test
    void test4notifyAtKeyWaypoint(){
        String busId = "bus4";
        Location keyWaypointLocation = new Location(40.404040, -50.505050, true, "HB");

        when(gpsService.getCurrentLocation(busId)).thenReturn(keyWaypointLocation);
        busTracker.updateBusLocation(busId);

        verify(notificationService).notifyPassengers(busIdCaptor.capture(), messageCaptor.capture());
        assertEquals(busId, busIdCaptor.getValue());

        String expectedMessage = "The bus has arrived at HB";
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void test5nullWaypointName(){
        String busId = "bus5";
        Location noName = new Location(40.404040, 50.505050, true, null);

        when(gpsService.getCurrentLocation(busId)).thenReturn(noName);
        busTracker.updateBusLocation(busId);

        verify(notificationService).notifyPassengers(eq(busId), messageCaptor.capture());
        String capturedMessage = messageCaptor.getValue();

        String expectedMessage = "The bus has arrived at null";
        assertEquals(expectedMessage, capturedMessage);
    }

    @Test
    void test6multipleNotifyKeyWaypoint(){
        String busId1 = "bus61";
        String busId2 = "bus62";
        Location keyLocation = new Location(40.404040, 50.505050, true, "UZH");

        when(gpsService.getCurrentLocation(busId1)).thenReturn(keyLocation);
        when(gpsService.getCurrentLocation(busId2)).thenReturn(keyLocation);

        busTracker.updateBusLocation(busId1);
        busTracker.updateBusLocation(busId2);

        verify(notificationService, times(2)).notifyPassengers(busIdCaptor.capture(), messageCaptor.capture());

        List<String> capturedBusIds = busIdCaptor.getAllValues();
        List<String> capturedMessages = messageCaptor.getAllValues();

        assertTrue(capturedBusIds.contains(busId1));
        assertTrue(capturedBusIds.contains(busId2));

        String expectedMessageForBothBuses = "The bus has arrived at UZH";
        assertEquals(expectedMessageForBothBuses, capturedMessages.get(capturedBusIds.indexOf(busId1)));
        assertEquals(expectedMessageForBothBuses, capturedMessages.get(capturedBusIds.indexOf(busId2)));
    }

    @Test
    void test7updateLocationGPSLoss(){
        String busId = "bus7";
        when(gpsService.getCurrentLocation(busId)).thenReturn(null);

        busTracker.updateBusLocation(busId);
        verify(notificationService).notifyPassengers(eq(busId), messageCaptor.capture());

        String expectedMessage = "GPS signal lost. Please check back later.";
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void test8multiplePSLosses(){
        String busId1 = "bus81";
        String busId2 = "bus82";
        String busId3 = "bus83";

        when(gpsService.getCurrentLocation(busId1)).thenReturn(null);
        when(gpsService.getCurrentLocation(busId2)).thenReturn(null);
        when(gpsService.getCurrentLocation(busId3)).thenReturn(null);

        busTracker.updateBusLocation(busId1);
        busTracker.updateBusLocation(busId2);
        busTracker.updateBusLocation(busId3);

        verify(notificationService, times(1)).notifyPassengers(eq(busId1), anyString());
        verify(notificationService, times(1)).notifyPassengers(eq(busId2), anyString());
        verify(notificationService, times(1)).notifyPassengers(eq(busId3), anyString());

        verify(notificationService, times(3)).notifyPassengers(anyString(), messageCaptor.capture());
        List<String> capturedMessages = messageCaptor.getAllValues();
        String expected = "GPS signal lost. Please check back later.";

        assertEquals(3, capturedMessages.size());
        assertTrue(capturedMessages.stream().allMatch(message -> message.equals(expected)));
    }
}
