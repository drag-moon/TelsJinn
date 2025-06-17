import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';
import { startAfterBlow, fetchVehicles } from './TeslaAPI';

export default function App() {
  const [vehicleId, setVehicleId] = useState('');
  const [token, setToken] = useState('');
  const [vehicles, setVehicles] = useState([]);
  const [status, setStatus] = useState('');

  const trigger = async () => {
    setStatus('Sending command...');
    try {
      const res = await startAfterBlow(vehicleId, token);
      if (res && res.response) {
        setStatus('After blow triggered');
      } else {
        setStatus('Command failed');
      }
    } catch (e) {
      setStatus('Error: ' + e.message);
    }
  };

  const loadVehicles = async () => {
    setStatus('Loading vehicles...');
    try {
      const data = await fetchVehicles(token);
      if (data && data.response && data.response.length > 0) {
        setVehicles(data.response);
        setVehicleId(String(data.response[0].id));
        setStatus('Loaded vehicle: ' + data.response[0].display_name);
      } else {
        setStatus('No vehicles found');
      }
    } catch (e) {
      setStatus('Error: ' + e.message);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Tesla After Blow</Text>
      <TextInput
        style={styles.input}
        placeholder="Vehicle ID"
        value={vehicleId}
        onChangeText={setVehicleId}
      />
      <TextInput
        style={styles.input}
        placeholder="Access Token"
        value={token}
        onChangeText={setToken}
      />
      <Button title="Load Vehicles" onPress={loadVehicles} />
      <Button title="Trigger After Blow" onPress={trigger} />
      {vehicleId ? (
        <Text style={styles.vehicle}>Selected Vehicle ID: {vehicleId}</Text>
      ) : null}
      <Text style={styles.status}>{status}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    alignItems: 'center',
    justifyContent: 'center'
  },
  title: {
    fontSize: 20,
    marginBottom: 12
  },
  input: {
    width: '80%',
    borderWidth: 1,
    padding: 8,
    marginBottom: 12
  },
  status: {
    marginTop: 12
  },
  vehicle: {
    marginTop: 12
  }
});
