import {useEffect, useState} from 'react'

interface WorkoutSession{
  id: number
  date: string
  notes: string
}

function App() {
  const [sessions, setSessions] = useState<WorkoutSession[]>([])
  const [date, setDate] = useState('')
  const [notes, setNotes] = useState('')


  useEffect(() => {
    fetch('http://localhost:8080/api/sessions')
    .then(res => res.json())
    .then(data => setSessions(data))
    .catch(err => console.error(err))
  }, [])

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const newSession = { date, notes }

    console.log('Sending:', newSession) 

    fetch('http://localhost:8080/api/sessions', {
      method: 'POST',
      headers: {
        'Content-Type' : 'application/json'
      },
      body: JSON.stringify(newSession),
    })
    .then(res => res.json())
    .then(addedSession => {
      setSessions(prev => [...prev, addedSession])
      setDate('')
      setNotes('')
    })
    .catch(err => console.error(err))
  }

  const handleDelete = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/sessions/${id}`, {
        method: 'DELETE',
      });
  
      if (response.ok) {
        setSessions((prevSessions) => prevSessions.filter((s) => s.id !== id));
      } else {
        console.error("Failed to delete session");
      }
    } catch (error) {
      console.error("Error deleting session:", error);
    }
  }

  return (
    <div>
      <h1>Workout Sessions</h1>
      <form onSubmit={handleSubmit}>
        <input type="date" value = {date} onChange={e => setDate(e.target.value)} required/>
        <input type='text' value = {notes} onChange={e => setNotes(e.target.value)  } required/>
        <button type  = 'submit' >Add session</button>
      </form>
      <ul>
        {sessions.map(session => (
          <li key={session.id}>
            {session.date}: {session.notes}
            <button onClick={() => handleDelete(session.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  )
}


export default App
