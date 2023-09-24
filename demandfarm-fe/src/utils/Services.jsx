import axios from "axios";
const controller = new AbortController();
export const instance = axios.create({
  baseURL: `${process.env.REACT_APP_BASEURL}/`,
  timeout: 20000,
  headers: {
    "Content-Type": "application/json",
  },
  signal: controller.signal,
});

export const RequestData = (method, uri) => {
  return instance({
    method: method,
    url: uri,
  })
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.error(error.response.data);
        throw new Error(error.response.data.errors[0].errorMessage);
      } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        console.error(error.request);
        throw new Error("No Response received from server. Please try again");
      } else {
        // Something happened in setting up the request that triggered an Error.
        console.error("Error", error.message);
        throw new Error("Something Unexpected happend. Please try again");
      }
    });
};
