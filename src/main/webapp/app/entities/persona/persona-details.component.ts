import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IPersona } from '@/shared/model/persona.model';

import PersonaService from './persona.service';

export default defineComponent({
  name: 'PersonaDetails',
  setup() {
    const personaService = inject('personaService', () => new PersonaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const persona: Ref<IPersona> = ref({});

    const retrievePersona = async personaId => {
      try {
        const res = await personaService().find(personaId);
        persona.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.personaId) {
      retrievePersona(route.params.personaId);
    }

    return {
      alertService,
      persona,

      previousState,
    };
  },
});
