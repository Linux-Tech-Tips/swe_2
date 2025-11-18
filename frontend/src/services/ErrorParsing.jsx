const ErrorMessage = (message) => {
  let messageArray = message.split(' ');
  messageArray[0] = '';
  return(messageArray.join(' '));
}

export {ErrorMessage};
