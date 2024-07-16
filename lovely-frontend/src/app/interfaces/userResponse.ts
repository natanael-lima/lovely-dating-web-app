import { PreferenceDTO } from "./preferenceDTO";
import { ProfileDetailDTO } from "./profileDetailDTO";

export interface UserResponse{
    id: number;
    username: string;
    lastname: string;
    name: string;
    preference: PreferenceDTO;
    profileDetail: ProfileDetailDTO;
  }
  