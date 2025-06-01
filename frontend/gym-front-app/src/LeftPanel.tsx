import { useState, useEffect, type ChangeEvent } from 'react';
import axios from 'axios';

const muscleGroupsOptions = [
    'LEGS', 'TRICEPS', 'BICEPS', 'SHOULDERS', 'CHEST', 'BACK'
];

interface Exercise {
    name: string;
}

interface Set {
    reps: string;
    weight: string;
}

interface SessionExercise {
    name: string;
    sets: Set[];
}

interface Session {
    id: number;
    name: string;
    dateTime: string;
    muscleGroups: string[];
    notes?: string;
    exercises: Exercise[];
    userId: number;
}

function LeftPanel() {
    const [creating, setCreating] = useState<boolean>(false);
    const [muscleGroups, setMuscleGroups] = useState<string[]>([]);
    const [exercisesFromBackend, setExercisesFromBackend] = useState<string[]>([]);
    const [sessionNotes, setSessionNotes] = useState<string>('');
    const [history, setHistory] = useState<Session[]>([]);
    const [dateTime, setDateTime] = useState<string | null>(null);
    const [sessionExercises, setSessionExercises] = useState<SessionExercise[]>([]);

    // Load session history from backend
    const loadHistory = async () => {
        try {
            const res = await axios.get<Session[]>('http://localhost:8080/api/sessions');
            setHistory(res.data);
        } catch (err) {
            console.error('Failed to load sessions', err);
        }
    };

    useEffect(() => {
        loadHistory();
    }, []);

    // When creating session, set current datetime
    useEffect(() => {
        if (creating) {
            setDateTime(new Date().toISOString().slice(0, 16)); // e.g. '2025-05-30T14:30'
        } else {
            setDateTime(null);
        }
    }, [creating]);

    // When muscle groups change, load exercises from backend
    useEffect(() => {
        if (muscleGroups.length === 0) {
            setExercisesFromBackend([]);
            return;
        }

        const fetchExercises = async () => {
            try {
                const groupsParam = muscleGroups.join(',');
                const res = await axios.get<string[]>(`http://localhost:8080/api/exercises?muscleGroups=${groupsParam}`);
                console.log('Fetched exercises', res.data);
                setExercisesFromBackend(res.data);
            } catch (err) {
                console.error('Failed to fetch exercises', err);
            }
        };

        fetchExercises();
    }, [muscleGroups]);

    // Handle muscle group selection change
    const onMuscleGroupsChange = (e: ChangeEvent<HTMLSelectElement>) => {
        const selected = Array.from(e.target.selectedOptions).map(o => o.value);
        setMuscleGroups(selected);
        setSessionExercises([]); // clear current exercises when muscle groups change
    };

    // Add exercise to session with empty default sets
    const addExerciseToSession = (exerciseName: string) => {
        setSessionExercises(prev => [
            ...prev,
            { name: exerciseName, sets: [{ reps: '', weight: '' }] }
        ]);
    };

    // Remove exercise from session
    const removeExerciseFromSession = (index: number) => {
        setSessionExercises(prev => prev.filter((_, i) => i !== index));
    };

    // Update reps or weight for a given exercise's set
    const updateSet = (exerciseIndex: number, setIndex: number, field: 'reps' | 'weight', value: string) => {
        setSessionExercises(prev => {
            const newExercises = [...prev];
            newExercises[exerciseIndex].sets[setIndex][field] = value;
            return newExercises;
        });
    };

    // Add a new set row to exercise
    const addSetRow = (exerciseIndex: number) => {
        setSessionExercises(prev => {
            const newExercises = [...prev];
            const sets = newExercises[exerciseIndex].sets;
            // Check if the last set is empty, prevent adding another empty one
            if (sets.length > 0 && sets[sets.length - 1].reps === '' && sets[sets.length - 1].weight === '') {
                return newExercises; // prevent duplicate insert
            }
            newExercises[exerciseIndex].sets = [...sets, { reps: '', weight: '' }];
            return newExercises;
        });
    };

    // Remove a set row from exercise
    const removeSetRow = (exerciseIndex: number, setIndex: number) => {
        console.log(`Removing set ${setIndex} from exercise ${exerciseIndex}`);
        setSessionExercises(prev => {
            const newExercises = [...prev];
            if (newExercises[exerciseIndex].sets.length > 1) {
                const updatedSets = newExercises[exerciseIndex].sets.filter((_, i) => i !== setIndex);
                newExercises[exerciseIndex] = {
                    ...newExercises[exerciseIndex],
                    sets: updatedSets
                };
            }
            return newExercises;
        });
    };

    // Handle session submission
    const onEndSessionClick = async () => {
        if (muscleGroups.length === 0) {
            alert('Select at least one muscle group');
            return;
        }
        if (sessionExercises.length === 0) {
            alert('Add at least one exercise');
            return;
        }

        const sessionName = muscleGroups.join(' + ') + ' workout';

        const exercisesPayload = sessionExercises.map(ex => ({
            name: ex.name,
            setsJson: JSON.stringify(ex.sets)
        }));

        const sessionPayload = {
            name: sessionName,
            dateTime: new Date(dateTime!).toISOString(),
            muscleGroups,

            notes: sessionNotes,
            exercises: exercisesPayload,
            userId: 1
        };

        try {
            console.log(sessionPayload.muscleGroups)
            await axios.post('http://localhost:8080/api/sessions', sessionPayload);
            alert('Session saved!');
            setCreating(false);
            setMuscleGroups([]);
            setSessionExercises([]);
            setSessionNotes('');
            setDateTime(null);
            loadHistory();
        } catch (err) {
            console.error('Failed to save session', err);
            alert('Error saving session');
        }
    };

    return (
        <div style={{ width: '50vw', padding: '1rem', boxSizing: 'border-box', fontFamily: 'Arial, sans-serif' }}>
            {!creating && <button onClick={() => setCreating(true)}>Create Session</button>}

            {creating && (
                <form onSubmit={e => e.preventDefault()} style={{ marginTop: '1rem' }}>
                    <div>
                        <label><b>Date & Time:</b></label><br />
                        <input type="datetime-local" value={dateTime || ''} readOnly style={{ width: '100%' }} />
                    </div>

                    <div style={{ marginTop: '1rem' }}>
                        <label><b>Muscle Groups:</b></label><br />
                        <select
                            multiple
                            value={muscleGroups}
                            onChange={onMuscleGroupsChange}
                            size={muscleGroupsOptions.length}
                            style={{ width: '100%' }}
                        >
                            {muscleGroupsOptions.map(group => (
                                <option key={group} value={group}>{group}</option>
                            ))}
                        </select>
                    </div>

                    <div style={{ marginTop: '1rem' }}>
                        <label><b>Exercises:</b></label><br />
                        <div style={{ maxHeight: '100px', overflowY: 'auto', border: '1px solid #ccc', padding: '0.5rem' }}>
                            {exercisesFromBackend.length === 0 && <p style={{ fontSize: '0.9rem' }}>Select muscle groups to load exercises.</p>}
                            {exercisesFromBackend.map(ex => (
                                <button
                                    key={ex}
                                    type="button"
                                    onClick={() => addExerciseToSession(ex)}
                                    style={{ marginRight: '0.5rem', marginBottom: '0.5rem' }}
                                >
                                    + {ex}
                                </button>
                            ))}
                        </div>

                        {sessionExercises.length === 0 && <p style={{ fontSize: '0.9rem', marginTop: '0.5rem' }}>No exercises added yet.</p>}

                        {sessionExercises.map((ex, i) => (
                            <div key={i} style={{ marginTop: '1rem', border: '1px solid #aaa', padding: '0.5rem' }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                    <strong>{ex.name}</strong>
                                    <button type="button" onClick={() => removeExerciseFromSession(i)} style={{ color: 'red' }}>Remove</button>
                                </div>

                                <table style={{ width: '100%', marginTop: '0.5rem' }}>
                                    <thead>
                                    <tr>
                                        <th>Set</th>
                                        <th>Reps</th>
                                        <th>Weight</th>
                                        <th>Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {ex.sets.map((set, si) => (
                                        <tr key={si}>
                                            <td>{si + 1}</td>
                                            <td>
                                                <input
                                                    type="number"
                                                    min="0"
                                                    value={set.reps}
                                                    onChange={e => updateSet(i, si, 'reps', e.target.value)}
                                                    style={{ width: '60px' }}
                                                />
                                            </td>
                                            <td>
                                                <input
                                                    type="number"
                                                    min="0"
                                                    step="0.1"
                                                    value={set.weight}
                                                    onChange={e => updateSet(i, si, 'weight', e.target.value)}
                                                    style={{ width: '80px' }}
                                                />
                                            </td>
                                            <td>
                                                <button
                                                    type="button"
                                                    onClick={() => removeSetRow(i, si)}
                                                    disabled={ex.sets.length === 1}
                                                >
                                                    Remove
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>

                                <button type="button" onClick={() => addSetRow(i)} style={{ marginTop: '0.5rem' }}>
                                    + Add Set
                                </button>
                            </div>
                        ))}
                    </div>

                    <div style={{ marginTop: '1rem' }}>
                        <label><b>Notes:</b></label><br />
                        <textarea
                            value={sessionNotes}
                            onChange={e => setSessionNotes(e.target.value)}
                            rows={3}
                            style={{ width: '100%' }}
                            placeholder="Optional notes"
                        />
                    </div>

                    <div style={{ marginTop: '1rem' }}>
                        <button type="button" onClick={onEndSessionClick} style={{ marginRight: '1rem' }}>End Session</button>
                        <button type="button" onClick={() => setCreating(false)}>Cancel</button>
                    </div>
                </form>
            )}

            <div style={{ marginTop: '2rem' }}>
                <h3>History</h3>
                {history.length === 0 && <p>No sessions yet.</p>}
                <ul>
                    {history.map(session => (
                        <li key={session.id}>
                            <strong>{session.name}</strong> — {new Date(session.dateTime).toLocaleString()}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default LeftPanel;
