import { type ICiudad } from '@/shared/model/ciudad.model';
import { type ISexo } from '@/shared/model/sexo.model';
import { type ITipoDocumento } from '@/shared/model/tipo-documento.model';

export interface IPersona {
  id?: number;
  nombre?: string;
  apellido?: string;
  nroDocumento?: string;
  fechaNacimiento?: Date | null;
  telefono?: string | null;
  email?: string | null;
  direccion?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  tipoDocumento?: ITipoDocumento;
  sexo?: ISexo;
  ciudad?: ICiudad | null;
}

export class Persona implements IPersona {
  constructor(
    public id?: number,
    public nombre?: string,
    public apellido?: string,
    public nroDocumento?: string,
    public fechaNacimiento?: Date | null,
    public telefono?: string | null,
    public email?: string | null,
    public direccion?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public tipoDocumento?: ITipoDocumento,
    public sexo?: ISexo,
    public ciudad?: ICiudad | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
