export class User {
  name: string;
  lastname: string;
  username: string;
  password: string;
  profile: UserProfile; // Agregar el perfil del usuario

  constructor(name: string, lastname: string, username: string, password: string) {
    this.name = name;
    this.lastname = lastname;
    this.username = username;
    this.password = password;
    this.profile = new UserProfile(); // Inicializar el perfil
  }
  
}

export class UserProfile {
  photo: File | null;
  photoFileName: string;
  location: string;
  gender: string;
  age: string;
  likeGender: string;
  maxAge: number;
  minAge: number;

  constructor() {
    this.photo = null;
    this.photoFileName = '';
    this.location = '';
    this.gender = '';
    this.age = '';
    this.likeGender = '';
    this.maxAge = 0;
    this.minAge = 0;
  }
}