export interface ProfileDetailDTO {
    id: number;
    phone: string;
    gender: string;
    birthDate: Date; // Puedes manejar la fecha como string o convertirla según tus necesidades
    description: string;
    work: string;
    photo: File | null;
    photoFileName: string;
    timestamp: string; // Puedes manejar la fecha como string o convertirla según tus necesidades
  }
  