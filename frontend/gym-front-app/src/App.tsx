import {useEffect, useState} from 'react'

interface WorkoutSession{
  id: number
  date: string
  notes: string
}

function App() {
  const [sessions, setSessions] = useState<WorkoutSession[]>([])

  useEffect(() => {
    fetch('http://localhost:8080/api/sessions')
    .then(res => res.json())
    .then(data => setSessions(data))
    .catch(err => console.error(err))
  }, [])

  return (
    <div>
      <h1>Workout Sessions</h1>
      <ul>
        {sessions.map(session => (
          <li key={session.id}>
            {session.date}: {session.notes}
          </li>
        ))}
      </ul>
    </div>
  )
}


export default App
