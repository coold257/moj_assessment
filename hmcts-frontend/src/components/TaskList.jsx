import { useState } from 'react';
import TaskCard from './TaskCard';

const TaskList = ({ tasks, onDelete, onEdit, onStatusChange }) => {
  const [filter, setFilter] = useState('ALL');
  
  // Filter tasks based on status
  const filteredTasks = filter === 'ALL' 
    ? tasks 
    : tasks.filter(task => task.taskStatus === filter);

  return (
    <div>
      <div className="mb-4 flex space-x-2">
        <button 
          onClick={() => setFilter('ALL')}
          className={`px-3 py-1 text-sm rounded ${filter === 'ALL' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
        >
          All
        </button>
        <button 
          onClick={() => setFilter('TODO')}
          className={`px-3 py-1 text-sm rounded ${filter === 'TODO' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
        >
          To Do
        </button>
        <button 
          onClick={() => setFilter('IN_PROGRESS')}
          className={`px-3 py-1 text-sm rounded ${filter === 'IN_PROGRESS' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
        >
          In Progress
        </button>
        <button 
          onClick={() => setFilter('COMPLETED')}
          className={`px-3 py-1 text-sm rounded ${filter === 'COMPLETED' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
        >
          Completed
        </button>
      </div>
      
      {filteredTasks.length === 0 ? (
        <div className="p-4 border rounded bg-gray-50 text-gray-500">
          No tasks found.
        </div>
      ) : (
        <div className="space-y-4">
          {filteredTasks.map(task => (
            <TaskCard 
              key={task.taskId} 
              task={task} 
              onDelete={onDelete}
              onEdit={onEdit}
              onStatusChange={onStatusChange}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default TaskList; 