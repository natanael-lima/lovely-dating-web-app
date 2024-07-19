import { UserDTO } from "./userDTO";
export interface NotificationDTO {
    id: number;
    content: string;
    time: string;
    isUnread: boolean;
    receiver: UserDTO;
    sender: UserDTO;
  }