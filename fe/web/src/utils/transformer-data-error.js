export const transformerDataErrorServer = (error) => {
  switch (error) {
    case 401:
      return "Username or password incorrect";

    case 403:
      return "You are not allowed to access";

    default:
      return "bad request";
  }
};
