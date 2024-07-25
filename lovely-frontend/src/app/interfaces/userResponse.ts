import { PreferenceDTO } from "./preferenceDTO";
import { ProfileDetailDTO } from "./profileDetailDTO";

export interface UserResponse{
    id: number;
    username: string;
    lastname: string;
    name: string;
    role: string;
    state: string;
    isVisible: Boolean;
    preference: PreferenceDTO;
    profileDetail: ProfileDetailDTO;
  }
  