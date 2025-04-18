import { useState } from 'react';

const TaskCard = ({ task, onDelete, onEdit, onStatusChange }) => {
  const [isDeleting, setIsDeleting] = useState(false);
  
  // Format the due date to a readable format
  const formatDate = (dateString) => {
    if (!dateString) return 'No due date';
    
    const date = new Date(dateString);
    return date.toLocaleString('en-GB', {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };
  
  // Get color based on task status
  const getStatusColor = (status) => {
    switch(status) {
      case 'TODO':
        return 'bg-yellow-100 text-yellow-800';
      case 'IN_PROGRESS':
        return 'bg-blue-100 text-blue-800';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };
  
  // Handle status change
  const handleStatusChange = (event) => {
    onStatusChange(task.taskId, event.target.value);
  };
  
  // Confirm deletion
  const confirmDelete = () => {
    setIsDeleting(true);
  };
  
  // Cancel deletion
  const cancelDelete = () => {
    setIsDeleting(false);
  };
  
  // Handle delete confirmation
  const handleDelete = () => {
    onDelete(task.taskId);
    setIsDeleting(false);
  };

  return (
    <div className="border rounded-lg shadow-sm bg-white overflow-hidden">
      <div className="p-4">
        <div className="flex justify-between items-start">
          <div>
            <h3 className="text-lg font-semibold">{task.taskTitle}</h3>
            <div className="mt-1">
              <span className={`inline-block px-2 py-1 text-xs rounded ${getStatusColor(task.taskStatus)}`}>
                {task.taskStatus.replace('_', ' ')}
              </span>
            </div>
          </div>
          <div className="flex space-x-2">
            <button
              onClick={() => onEdit(task)}
              className="p-1 text-blue-600 hover:text-blue-800"
            >
              Edit
            </button>
            <button
              onClick={confirmDelete}
              className="p-1 text-red-600 hover:text-red-800"
            >
              Delete
            </button>
          </div>
        </div>
        
        <p className="mt-2 text-gray-700">{task.taskDescription}</p>
        
        <div className="mt-3 text-sm text-gray-500">
          Due: {formatDate(task.dueDateTime)}
        </div>
      </div>
      
      <div className="border-t px-4 py-3 bg-gray-50">
        <div className="flex items-center">
          <label htmlFor={`status-${task.taskId}`} className="text-sm mr-2">
            Status:
          </label>
          <select
            id={`status-${task.taskId}`}
            value={task.taskStatus}
            onChange={handleStatusChange}
            className="border rounded p-1 text-sm"
          >
            <option value="TODO">To Do</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
          </select>
        </div>
      </div>
      
      {isDeleting && (
        <div className="p-3 bg-red-50 border-t">
          <p className="text-sm mb-2">Are you sure you want to delete this task?</p>
          <div className="flex space-x-2">
            <button 
              onClick={handleDelete}
              className="px-3 py-1 bg-red-600 text-white text-sm rounded"
            >
              Yes, Delete
            </button>
            <button 
              onClick={cancelDelete}
              className="px-3 py-1 bg-gray-200 text-gray-800 text-sm rounded"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TaskCard; 