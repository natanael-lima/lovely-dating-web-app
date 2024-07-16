import { PreferenceDTO } from "./preferenceDTO";
import { ProfileDetailDTO } from "./profileDetailDTO";

export interface UserDTO {
    id: number;
    username: string;
    password: string;
    lastname: string;
    name: string;
    role: string;
    preference: PreferenceDTO;
    profileDetail: ProfileDetailDTO;
  }
  