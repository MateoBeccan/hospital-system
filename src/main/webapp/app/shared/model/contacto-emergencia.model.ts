import { type IPersona } from '@/shared/model/persona.model';

export interface IContactoEmergencia {
  id?: number;
  nombre?: string;
  telefono?: string;
  parentesco?: string | null;
  observaciones?: string | null;
  prioridad?: number;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  persona?: IPersona;
}

export class ContactoEmergencia implements IContactoEmergencia {
  constructor(
    public id?: number,
    public nombre?: string,
    public telefono?: string,
    public parentesco?: string | null,
    public observaciones?: string | null,
    public prioridad?: number,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public persona?: IPersona,
  ) {
    this.activo = this.activo ?? false;
  }
}
