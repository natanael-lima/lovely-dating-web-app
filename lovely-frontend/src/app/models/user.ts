export class User {
  name: string;
  lastname: string;
  username: string;
  password: string;

  constructor(name: string, lastname: string, username: string, password: string) {
    this.name = name;
    this.lastname = lastname;
    this.username = username;
    this.password = password;
  }
}