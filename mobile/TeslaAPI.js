export async function startAfterBlow(vehicleId, token) {
  const url = `https://owner-api.teslamotors.com/api/1/vehicles/${vehicleId}/command/after_blow`; // Example endpoint

  const res = await fetch(url, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });

  return res.json();
}

// Fetch the authenticated user's vehicles
export async function fetchVehicles(token) {
  const res = await fetch('https://owner-api.teslamotors.com/api/1/vehicles', {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });

  return res.json();
}
