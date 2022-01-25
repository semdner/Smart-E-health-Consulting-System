let mapReady = false;
let dataReady = false;
let mapLoaded = false;
let originAddress = "";
let destinationAddress = "";
let centerLat = 0.0;
let centerLng = 0.0;
let zoomLevel = 4;

function mapCallback() {
    mapReady = true;
    if (dataReady && !mapLoaded)
        initMap();
}

function setData(cLat, cLng, origin, destination, zoom) {
    centerLat = cLat;
    centerLng = cLng;

    originAddress = origin;
    destinationAddress = destination;

    zoomLevel = zoom;

    dataReady = true;
    if (mapReady && !mapLoaded)
        initMap();
}

function initMap() {
    mapLoaded = true;
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: zoomLevel,
        center: { lat: centerLat, lng: centerLng },
    });
    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer({
        draggable: true,
        map,
        panel: document.getElementById("panel"),
    });

    directionsRenderer.addListener("directions_changed", () => {
        const directions = directionsRenderer.getDirections();

        if (directions) {
            computeTotalDistance(directions);
        }
    });
    displayRoute(originAddress,destinationAddress, directionsService, directionsRenderer);
}

function displayRoute(origin, destination, service, display) {
    service
        .route({
            origin: origin,
            destination: destination,
            travelMode: google.maps.TravelMode.DRIVING,
            avoidTolls: true,
        })
        .then((result) => {
            display.setDirections(result);
        })
        .catch((e) => {
            alert("Could not display directions due to: " + e);
        });
}

function computeTotalDistance(result) {
    let total = 0;
    const myroute = result.routes[0];

    if (!myroute) {
        return;
    }

    for (let i = 0; i < myroute.legs.length; i++) {
        total += myroute.legs[i].distance.value;
    }

    total = total / 1000;
    document.getElementById("total").innerHTML = total + " km";
}
