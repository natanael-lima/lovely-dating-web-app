import { Message } from "../models/message";

export interface MessageRequest{
    id: number;
    content: string;
    timestamp: string;
    senderId: number;
    chatId: number;
}